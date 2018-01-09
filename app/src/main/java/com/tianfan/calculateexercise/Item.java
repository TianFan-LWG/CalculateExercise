package com.tianfan.calculateexercise;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;

/**
 * Created by 天蘩 on 2017/12/23.
 */
public class Item extends LinearLayout {
    //View layout;
    TextView textView1, textView2, textView3;
    EditText edtxt_result;
    String result = "", validResult;//正确结果
    int dot1 = -1, dot2 = 0;//点标记
    double validResultNum;//完整答案
    boolean isShowResult = false;//已经显示答案标记
    public boolean getisShowResult(){return isShowResult;}
    public enum CheckMode {
        CM_decimals, CM_integer;
    }

    CheckMode checkMode;

    public Item(Context context, String formula, double numresult, CheckMode checkmode) {
        super(context);
        /*
        layout = LayoutInflater.from(context).inflate(R.layout.item, null);
        textView1 = layout.findViewById(0);
        textView2 = layout.findViewById(2);
        edtxt_result = layout.findViewById(1);
        */

        if (checkmode == null)
            this.checkMode = CheckMode.CM_decimals;
        else
            this.checkMode = checkmode;

        if (checkMode == CheckMode.CM_integer) {
            //validResult=String.valueOf(Math.floor(numresult));
            validResult = String.format("%.0f", numresult);
        } else {
            //保留两位小数
            validResult = String.format("%.2f", numresult);
        }
        validResultNum = numresult;//记录完整答案

        textView1 = new TextView(context);
        textView1.setTextColor(Color.BLACK);
        textView1.setTextSize(18);
        textView1.setText(formula);//填充式子

        textView2 = new TextView(context);
        textView2.setTextColor(Color.RED);
        textView2.setTextSize(24);

        textView3 = new TextView(context);

        edtxt_result = new EditText(context);
        edtxt_result.setTextColor(Color.BLACK);
        if (checkmode == CheckMode.CM_integer) {
            //只能输入数字
            edtxt_result.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else {
            //只能输入数字、点
            edtxt_result.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
        }
        edtxt_result.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edtxt_result.removeTextChangedListener(this);
                //限制点的个数
                SetDot();
                edtxt_result.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        LayoutParams lp = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);
        edtxt_result.setLayoutParams(lp);//设置权重为1

        this.addView(textView1);
        this.addView(edtxt_result);
        this.addView(textView2);
        this.addView(textView3);
    }

    /**
     * 单一点
     */
    private void SetDot() {
        result = edtxt_result.getText().toString();
        dot1 = result.indexOf('.');
        //当点在最前面时换为0.
        if (dot1 == 0) {
            result = "0.";
            dot1 = 1;
        }
        //只允许输入一个点
        if (dot1 != -1) {
            dot2 = result.lastIndexOf('.');
            if (dot1 != dot2) {
                result = result.substring(0, dot2) + result.substring(dot2 + 1);
            }
        }
        if (!edtxt_result.getText().toString().equals(result)) {
            edtxt_result.setText(result);
            //设置光标到末尾
            if (edtxt_result.getText() instanceof Spannable) {
                Spannable spanText = (Spannable) edtxt_result.getText();
                Selection.setSelection(spanText, edtxt_result.getText().length());
            }
        }
    }

    public boolean CheckResult() {
        return CheckResult(this.checkMode);
    }

    /**
     * 检查结果是否正确
     *
     * @param mode 判断模式
     * @return 判断结果
     */
    public boolean CheckResult(CheckMode mode) {
        if (result == ""||result==null)return false;
        if (isShowResult) return false;//如果已经显示了答案就不再判断
        isShowResult=true;
        setEditTextReadOnly();
        textView3.setText("答案:" + validResult);
        if (result == "0.") result = "0.0";
        if (mode == null) mode = this.checkMode;
        switch (mode) {
            case CM_integer:
                if (result.equals(validResult)) {
                    textView2.setText("✔");
                    return true;
                } else {
                    textView2.setText("✘");
                    return false;
                }
            case CM_decimals:
                String temp = new BigDecimal(result)
                        .setScale(2, BigDecimal.ROUND_HALF_UP)
                        .toString();//四舍5入
                if (temp.equals(validResult)) {
                    textView2.setText("✔");
                    return true;
                } else {
                    textView2.setText("✘");
                    return false;
                }
            default:
                return false;
        }

    }

    /**
     * 设置只读
     */
    public void setEditTextReadOnly() {
        edtxt_result.setTextColor(Color.GRAY);//设置只读时的文字颜色
        //已经提交过的设置为只读模式
        if (edtxt_result instanceof EditText) {
            edtxt_result.setCursorVisible(false);//设置输入框中的光标不可见
            edtxt_result.setFocusable(false);//无焦点
            edtxt_result.setFocusableInTouchMode(false);//触摸时也得不到焦点
        }
    }

    /**
     * 显示完整答案
     */
    public void ShowResult() {
        setEditTextReadOnly();
        textView3.setText("完整答案：" + String.format("%f", validResultNum));
        isShowResult = true;
    }
}
