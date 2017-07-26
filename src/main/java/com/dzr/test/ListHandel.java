package com.dzr.test;

import com.dzr.po.User;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
 * @author dingzr
 * @Description 集合的处理
 * @ClassName ListHandel
 * @since 2017/5/31 17:05
 */
public class ListHandel {

    /**
     * 集合去重方法
     */
    private static void distinct(){
        List<User> list = SteamTest.getList(3);
        List<User> unique = list.stream()
                .collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingInt(User::getAge))), ArrayList::new));

        System.err.println(unique.size());
    }

    public static void main(String[] args) {
        distinct();
    }

}
