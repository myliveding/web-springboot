package com.dzr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author dingzr
 * @Description 用于有样式的页面或者模版渲染
 * @ClassName UserHtmlController
 * @since 2017/6/29 14:31
 */

@Controller
@RequestMapping("/userhtml")
public class UserHtmlController {

    @RequestMapping("/{name}")
    public String hello(@PathVariable("name") String name, Model model) {
        model.addAttribute("name", name);
        return "hello";
    }

}
