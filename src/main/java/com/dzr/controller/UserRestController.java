package com.dzr.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * @author dingzr
 * @Description 直接push到浏览器页面 直接获取的是接口的返回信息
 * restful架构，是一种规范，或者说一种命名风格。简单的比如添加、删除、获取信息、获取列表等，
 * /book/add、/book/delete、/book/{id}/info、/book/list等。
 * @ClassName UserController
 * @since 2017/6/29 13:50
 */

@RestController // == @Controller + @ResponseBody
@RequestMapping("/userrest")
public class UserRestController {

    @RequestMapping("/index")
    public String greeting() {
        return "Hello World!";
    }

    @RequestMapping("/{username}")
    public String userProfile(@PathVariable("username") String username) {
        return String.format("user %s", username);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginGet() {
        return "Login Page";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginPost() {
        return "Login Post Request";
    }

}
