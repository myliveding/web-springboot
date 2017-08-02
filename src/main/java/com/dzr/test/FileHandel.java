package com.dzr.test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author dingzr
 * @Description
 * @ClassName FileHandel
 * @since 2017/5/27 17:07
 */
public class FileHandel {

    private static void readFile(){
        try{
            // Java8用流的方式读文件，更加高效
            Files.lines(Paths.get("D:\\upload\\微信号.txt"), StandardCharsets.UTF_8).forEach(System.out::println);
        }catch (IOException e){

        }
    }


    public static void main(String[] args) throws Exception{
        readFile();
        System.err.println();
    }
}
