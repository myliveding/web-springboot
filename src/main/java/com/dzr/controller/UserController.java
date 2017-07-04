package com.dzr.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;

/**
 * @author dingzr
 * @Description 用于有样式的页面或者模版渲染 jsp页面
 * @ClassName UserController
 * @since 2017/6/29 14:31
 */

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping("/{name}")
    public String hello(@PathVariable("name") String name, Model model) {
        model.addAttribute("name", name);
        return "welcome";
    }

    @RequestMapping("/index")
    public String welcome(Model model) {
        logger.info("测试log日志的位置。。。。");
        model.addAttribute("time", new Date());
        model.addAttribute("message", "第一个jsp页面");
        return "index";
    }

    @RequestMapping("/body")
    public @ResponseBody String body(Model model) {
        return "Controller里面的调用返回值";
    }

    @RequestMapping("/foo")
    public String foo(Map<String, Object> model) {
        throw new RuntimeException("Foo");
    }

}
