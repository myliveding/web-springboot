package com.dzr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description
 * @FileName IndexController
 * @Author dingzr
 * @CreateTime 2017/8/20 22:26 八月
 */

@Controller
public class IndexController {

    @RequestMapping("/")
    public String index() {
        return "bshow";
    }
}
