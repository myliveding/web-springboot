package com.dzr.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dingzr
 * @Description 直接push到浏览器页面
 * @ClassName UserController
 * @since 2017/6/29 13:50
 */

@RestController
@RequestMapping("/user")
public class UserController {

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
