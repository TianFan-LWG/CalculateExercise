package com.tianfan.calculateexercise;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class ExamActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    String operationMode = "";//小数模式
    String operationType="";//计算类型
    String negative="";//正负
    int maxNum=10;//最大数
    int countNum=30;//出题数量
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        //获取数组
        Bundle bundle = this.getIntent().getExtras();
        String[] strarr = bundle.getStringArray(MainActivity.KEY_EXAM);

        maxNum=Integer.parseInt(strarr[0]);
        operationType=strarr[1];
        operationMode =strarr[2];
        countNum=Integer.parseInt(strarr[3]);
        negative=strarr[4];
        //设置标题
        ((TextView) findViewById(R.id.txtv_title))
                .setText(strarr[0] + "以内的" + strarr[1] + "算数题(" + negative+"+"+operationMode+")");
        //绑定引用
        linearLayout = findViewById(R.id.llout_items);
        CreateFormula(countNum, maxNum, "加法");
    }

    public void Submit(View view) {
        int count = linearLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            Item item = (Item) linearLayout.getChildAt(i);
            item.CheckResult();
        }
    }

    //开新线程

    /**
     * 生成式子
     */
    private void CreateFormula(int count, int maxnum, String operationMode) {
        Random rd = new Random();
        double result = 0, num1 = 0, num2 = 0;//结果 运算数1 运算数2
        String formula = "";//式子
        for (int i = 0; i < count; i++) {
            switch (operationMode) {
                case "加法":
                    double sum = rd.nextInt(maxnum + 1);//结果
                    num1 = rd.nextInt((int) sum);//加数1
                    num2 = sum - num1;//加数2
                    formula =String.format("%.0f",num1) + " + " + String.format("%.0f",num2) + " = ";
                    result = sum;
                    break;
                case "减法":
                    num1 = rd.nextInt(maxnum + 1);//被减数
                    num2 = rd.nextInt((int) num1);
                    double sub = num1 - num2;
                    formula = String.format("%.0f",num1) + " - " + String.format("%.0f",num2) + " = ";
                    result=sub;
                    break;
                case "乘法":
                    break;
                case "除法":
                    break;
                case "加减":
                    break;
                case "乘除":
                    break;
                case "混合":
                    break;
            }

            Item item = new Item(getApplicationContext(), formula, result, Item.CheckMode.CM_integer);
            linearLayout.addView(item);
        }
    }
}
