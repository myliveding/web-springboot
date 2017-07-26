package com.dzr.test;

import java.util.UUID;

/**
 * @author dingzr
 * @Description
 * @ClassName Test
 * @since 2017/6/1 15:45
 */
public class Test {
    public static void main(String[] args) {
        //Integer 型比较假如是使用 == ，只能比较数值为-128~127数值; 在这个范围内使用的是自动装箱拆箱；
        //.intValue()使用这个需要确认属性不为null;
        //equals()使用这个也需要判断null；

        String a = "http://wx.qlogo.cn/mmopen/icsUOd93CglOLnKae5WdGO2wuMLK37ZUFc9wEFCEhdkTAQWZNTe6eibJvXOF63zVQmA6LfdsLK4XmUlibbRassfydY44BHX6Xt5/0";
        System.err.println(a.length());

        System.err.println(UUID.randomUUID().toString());
    }
}
