package com.dzr.util;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dingzr on 2017/4/10.10:28
 */
public class BigDecimalStudy {

    // 进行加法运算
    public static double add(double d1, double d2) {
        BigDecimal b1 = BigDecimal.valueOf(d1);
        BigDecimal b2 = BigDecimal.valueOf(d2);
        return b1.add(b2).doubleValue();
    }

    // 进行减法运算
    public static double sub(double d1, double d2) {
        BigDecimal b1 = BigDecimal.valueOf(d1);
        BigDecimal b2 = BigDecimal.valueOf(d2);
        return b1.subtract(b2).doubleValue();
    }

    // 进行乘法运算
    public static double mul(double d1, double d2) {
        BigDecimal b1 = BigDecimal.valueOf(d1);
        BigDecimal b2 = BigDecimal.valueOf(d2);
        return b1.multiply(b2).doubleValue();
    }

    // 进行除法运算
    public static double div(double d1, double d2, int len) {
        BigDecimal b1 = BigDecimal.valueOf(d1);
        BigDecimal b2 = BigDecimal.valueOf(d2);
        return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    // 进行加法运算
    public static BigDecimal add(BigDecimal d1, BigDecimal d2) {
        return d1.add(d2);
    }

    // 进行减法运算
    public static BigDecimal sub(BigDecimal d1, BigDecimal d2) {
        return d1.subtract(d2);
    }

    // 进行乘法运算
    public static BigDecimal mul(BigDecimal d1, BigDecimal d2) {
        return d1.multiply(d2);
    }

    // 进行除法运算
    public static BigDecimal div(BigDecimal d1, BigDecimal d2, int len) {
        return d1.divide(d2, len, BigDecimal.ROUND_HALF_UP);
    }

    // 进行四舍五入操作
    public static double round(double d, int len) {
        // 进行四舍五入操作
        BigDecimal b1 = BigDecimal.valueOf(d);
        BigDecimal b2 = BigDecimal.valueOf(1);
        // 任何一个数字除以1都是原数字
        // ROUND_HALF_UP是BigDecimal的一个常量， 表示进行四舍五入的操作
        return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    // 进行四舍五入操作
    public static double round(BigDecimal d, int len) {
        // 进行四舍五入操作
        BigDecimal b2 = BigDecimal.valueOf(1);
        // 任何一个数字除以1都是原数字
        // ROUND_HALF_UP是BigDecimal的一个常量， 表示进行四舍五入的操作
        return d.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    // 进行四舍五入操作
    public static BigDecimal roundBig(BigDecimal d, int len) {
        // 进行四舍五入操作
        BigDecimal b2 = BigDecimal.valueOf(1);
        // 任何一个数字除以1都是原数字
        // ROUND_HALF_UP是BigDecimal的一个常量， 表示进行四舍五入的操作
        return d.divide(b2, len, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * @Title: mantissa
     * @Description: 计算尾数规则方法
     * @param type
     *            1、四舍五入到元 2、四舍五入到角 3、四舍五入到分 4、见分进角 5、见角进元 6、取整到元 7、取整到角 8、取整到分
     *            9、四舍五入保留五位小数 10 见厘进分
     * @param number
     *            传入的数值
     * @return
     */
    public static BigDecimal mantissaCount (int type, String number) {
        BigDecimal res = BigDecimal.valueOf(0.00);
        switch (type) {
            case 1:
                res = new BigDecimal(number).setScale(0, BigDecimal.ROUND_HALF_UP);
                break;

            case 2:
                res = new BigDecimal(number).setScale(1, BigDecimal.ROUND_HALF_UP);
                break;

            case 3:
                res = new BigDecimal(number).setScale(2, BigDecimal.ROUND_HALF_UP);
                break;

            case 4:
                double c = Math.ceil(Double.parseDouble(number) * 10);
                res = BigDecimal.valueOf(div(c, 10, 6)).setScale(1, BigDecimal.ROUND_HALF_UP);
                break;

            case 5:
                res = BigDecimal.valueOf(Math.ceil(Double.parseDouble(number)));
                break;

            case 6:
                res = BigDecimal.valueOf(Math.floor(Double.parseDouble(number)));
                break;

            case 7:
                double a = Math.floor(Double.parseDouble(number) * 10);
                res = BigDecimal.valueOf(div(a, 10, 6)).setScale(1, BigDecimal.ROUND_HALF_UP);
                break;

            case 8:
                double b = Math.floor(Double.parseDouble(number) * 100);
                res = BigDecimal.valueOf(div(b, 100, 6)).setScale(2, BigDecimal.ROUND_HALF_UP);
                break;

            case 9:
                res = new BigDecimal(number).setScale(5, BigDecimal.ROUND_HALF_UP);
                break;

            case 10:
//                double d = Math.ceil(Double.parseDouble(number) * 100);
//                res = BigDecimal.valueOf(div(d, 100, 6)).setScale(2, BigDecimal.ROUND_HALF_UP);
                res = new BigDecimal(number).setScale(2, BigDecimal.ROUND_UP);
                break;

            default:
                break;
        }

        return res;
    }

    public static boolean isNumber(BigDecimal str){
        Pattern pattern=Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
        Matcher match = pattern.matcher(str.toString());
        if(match.matches()==false){
            return false;
        }else{
            return true;
        }
    }

    /**
     * main函数
     * @param args
     */
    public static void main(String[] args) {
        StringBuffer remarkHead = new StringBuffer("ddddddd，");
        StringBuffer remarkTail = new StringBuffer("aaaaaa，");
        StringBuffer tips = new StringBuffer(remarkHead.substring(0, remarkHead.length() - 1))
                .append(remarkTail.substring(0, remarkTail.length()-1)).append("”；");
        System.err.println(tips);

    }

}
