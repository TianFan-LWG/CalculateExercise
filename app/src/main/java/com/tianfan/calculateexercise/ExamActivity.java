package com.tianfan.calculateexercise;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ExamActivity extends AppCompatActivity {
    LinearLayout llout_items;//式子结合
    String operationMode = "";//小数模式
    String operationType = "";//计算类型
    String negative = "";//正负
    int maxNum = 10;//最大数
    int countNum = 30;//出题数量
    int isShowResultNum = 0;//已经判断过的数量
    boolean[] isShowResultArr;//标记已经判断过的
    int accuracyNum = 0;//正确题数
    double scoreNum;//单题分数

    TextView txtv_score;//得分
    TextView txtv_progress;//进度
    TextView txtv_accuracy;//正确比

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        //绑定引用
        llout_items = findViewById(R.id.llout_items);
        txtv_score = findViewById(R.id.txtv_score);
        txtv_progress = findViewById(R.id.txtv_progress);
        txtv_accuracy = findViewById(R.id.txtv_accuracy);

        //获取数组
        Bundle bundle = this.getIntent().getExtras();
        String[] strarr = bundle.getStringArray(MainActivity.KEY_EXAM);
        maxNum = Integer.parseInt(strarr[0]);
        operationType = strarr[1];
        operationMode = strarr[2];
        countNum = Integer.parseInt(strarr[3]);
        negative = strarr[4];
        //设置标题
        ((TextView) findViewById(R.id.txtv_title))
                .setText(strarr[0] + "以内的" + strarr[1] + "算数题(" + negative + "+" + operationMode + ")");
        //初始化进度
        txtv_progress.setText("0/" + strarr[3]);
        //初始化正确比
        txtv_accuracy.setText("0/" + strarr[3]);
        //初始化标记数组
        isShowResultArr = new boolean[countNum];
        //计算单题分数
        scoreNum = 100.00 / countNum;
        //生成式子
        CreateFormula(countNum, maxNum, strarr[1]);
    }

    public void Submit(View view) {
        int count = llout_items.getChildCount();
        boolean bl = false;
        for (int i = 0; i < count; i++) {
            Item item = (Item) llout_items.getChildAt(i);
            //如果当前项未判断过则执行判断，并记录判断结果
            if (!item.getisShowResult()) {
                bl = item.CheckResult();
            }
            //如果已经判断过并且未被标记过则计入标记
            if (item.getisShowResult() && !isShowResultArr[i]) {
                isShowResultNum++;
                isShowResultArr[i] = true;
                txtv_progress.setText(isShowResultNum + "/" + String.valueOf(countNum));
                //记录正确量
                if (bl) {
                    accuracyNum++;
                    //更新真确比
                    txtv_accuracy.setText(accuracyNum + "/" + String.valueOf(countNum));
                    //更新得分
                    txtv_score.setText(String.format("%.2f", accuracyNum * scoreNum));
                }
                //全对情况
                if (accuracyNum == countNum) {
                    txtv_score.setText("100");
                }
            }
        }
    }

    public void ShowResult(View view) {
        int count = llout_items.getChildCount();
        for (int i = 0; i < count; i++) {
            Item item = (Item) llout_items.getChildAt(i);
            item.ShowResult();
        }
    }

    /**
     * 生成式子(整数模式)
     */
    private void CreateFormula(int count, int maxnum, String operationMode) {
        llout_items.removeAllViewsInLayout();
        List<String> formulalist=new ArrayList<>();
        Random rd = new Random();
        double result = 0, num1 = 0, num2 = 0;//结果 运算数1 运算数2
        String formula = "";//式子
        double[] nums;
        for (int i = 0; i < count; i++) {
            switch (operationMode) {
                case "加法":
                    nums = jiajian(rd, maxnum);
                    num1 = nums[0];
                    num2 = nums[1];
                    result = nums[2];
                    formula = String.format("%.0f", num1) + " ＋ " + String.format("%.0f", num2) + " = ";
                    break;
                case "减法":
                    nums = jiajian(rd, maxnum);
                    num1 = nums[2];
                    num2 = nums[0];
                    result = nums[1];
                    formula = String.format("%.0f", num1) + " － " + String.format("%.0f", num2) + " = ";
                    break;
                case "乘法":
                    nums = chengchu(rd, maxnum);
                    num1 = nums[0];
                    num2 = nums[1];
                    result = nums[2];
                    formula = String.format("%.0f", num1) + " × " + String.format("%.0f", num2) + " = ";
                    break;
                case "除法":
                    nums = chengchu(rd, maxnum);
                    num1 = nums[2];
                    num2 = nums[0];
                    result = nums[1];
                    //如果除数为0则将除数和结果切换
                    if (num2 == 0) {
                        num2 = result;
                        result = 0;
                    }
                    formula = String.format("%.0f", num1) + " ÷ " + String.format("%.0f", num2) + " = ";
                    break;
                case "加减":
                    if (rd.nextInt(10) % 2 == 0) {
                        nums = jiajian(rd, maxnum);
                        num1 = nums[0];
                        num2 = nums[1];
                        result = nums[2];
                        formula = String.format("%.0f", num1) + " ＋ " + String.format("%.0f", num2) + " = ";
                    } else {
                        nums = jiajian(rd, maxnum);
                        num1 = nums[2];
                        num2 = nums[0];
                        result = nums[1];
                        formula = String.format("%.0f", num1) + " － " + String.format("%.0f", num2) + " = ";
                    }

                    break;
                case "乘除":
                    if (rd.nextInt(10) % 2 == 0) {
                        nums = chengchu(rd, maxnum);
                        num1 = nums[0];
                        num2 = nums[1];
                        result = nums[2];
                        formula = String.format("%.0f", num1) + " × " + String.format("%.0f", num2) + " = ";
                    } else {
                        nums = chengchu(rd, maxnum);
                        num1 = nums[2];
                        num2 = nums[0];
                        result = nums[1];
                        //如果除数为0则将除数和结果切换
                        if (num2 == 0) {
                            num2 = result;
                            result = 0;
                        }
                        formula = String.format("%.0f", num1) + " ÷ " + String.format("%.0f", num2) + " = ";
                    }
                    break;
                case "混合":
                    switch (rd.nextInt(20) % 4) {
                        case 0:
                            nums = jiajian(rd, maxnum);
                            num1 = nums[0];
                            num2 = nums[1];
                            result = nums[2];
                            formula = String.format("%.0f", num1) + " ＋ " + String.format("%.0f", num2) + " = ";
                            break;
                        case 1:
                            nums = jiajian(rd, maxnum);
                            num1 = nums[2];
                            num2 = nums[0];
                            result = nums[1];
                            formula = String.format("%.0f", num1) + " － " + String.format("%.0f", num2) + " = ";
                            break;
                        case 2:
                            nums = chengchu(rd, maxnum);
                            num1 = nums[0];
                            num2 = nums[1];
                            result = nums[2];
                            formula = String.format("%.0f", num1) + " × " + String.format("%.0f", num2) + " = ";
                            break;
                        case 3:
                            nums = chengchu(rd, maxnum);
                            num1 = nums[2];
                            num2 = nums[0];
                            result = nums[1];
                            //如果除数为0则将除数和结果切换
                            if (num2 == 0) {
                                num2 = result;
                                result = 0;
                            }
                            formula = String.format("%.0f", num1) + " ÷ " + String.format("%.0f", num2) + " = ";
                            break;
                    }
                    break;
            }
            //如果列表中已经包含式子则重新生成
            if(formulalist.contains(formula)){
                i--;
            }else {

                formulalist.add(formula);
                Item item = new Item(getApplicationContext(), formula, result, Item.CheckMode.CM_integer);
                llout_items.addView(item);
            }
        }
    }

    private void addition() {

    }

    private void subtraction() {

    }

    private void division() {

    }

    private void multiplication() {

    }

    //加减
    private double[] jiajian(Random rd, int maxnum) {
        double num1 = 0, num2 = 0;
        double sum = rd.nextInt(maxnum + 1);//结果
        num1 = rd.nextInt((int) sum+1);//加数1
        num2 = sum - num1;//加数2
        return new double[]{num1, num2, sum};
    }

    //乘除
    private double[] chengchu(Random rd, int maxnum) {
        double num1 = 0, num2 = 0;
        double product = rd.nextInt(maxnum + 1);//结果
        double result = product;
        List<Integer> list1 = new ArrayList<>();//因子列表
        if (product == 0) {
            num1 = rd.nextInt(maxnum + 1);
            num2 = rd.nextInt(maxnum + 1);
            if (num1 < num2) {
                num1 = 0;
            } else {
                num2 = 0;
            }
        } else {
            //分解因式
            for (int j = 2; j <= product; j++) {
                while (product != j) {//循环找因子
                    if (product % j == 0) //可以整除的数
                    {
                        list1.add(j);
                        product = product / j; //新的除数
                    } else
                        break;
                }
            }
            list1.add((int) product);//最后一个因子即最后的除数
            //随机个因数相乘作为第一个乘数
            num1 = 1;
            num2 = 0;//暂且用num2来计数
            int j1 = rd.nextInt(list1.size()) + 1;//随机个数
            do {
                int j2 = rd.nextInt(list1.size());//获取列表的随机位置
                num1 *= list1.get(j2);
                list1.remove(j2);//移除已经累乘过的因子
                num2++;
                if (num2 == j1) break;
            } while (list1.size() > 1);
            num2 = result / num1;
        }
        return new double[]{num1, num2, result};
    }
}
