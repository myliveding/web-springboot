package com.dzr.controller;

import com.dzr.framework.config.UrlConfig;
import com.dzr.po.User;
import com.dzr.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
@RequestMapping("/userJsp")
public class UserJspController {

    private static final Logger logger = LoggerFactory.getLogger(UserJspController.class);

    @Autowired
    UserService userService;
    @Autowired
    UrlConfig urlConfig;

    @RequestMapping("/{name}")
    public String welcome(@PathVariable("name") String name, Model model) {
        model.addAttribute("name", name);
        return "welcome";
    }

    @RequestMapping("/index")
    public String index(Model model) {
        logger.info("测试log日志的位置。。。。");
        model.addAttribute("time", new Date());
        model.addAttribute("message", "第一个jsp页面");
        logger.info("从配置文件中获取PHP调用地址：" + urlConfig.getPhp());
        User user = userService.getUserCityInfo(1, 1);
        model.addAttribute("address", user.getAddress());
        model.addAttribute("cityName", user.getCity().getName());
        return "index";
    }

    @RequestMapping("/json")
    public @ResponseBody User json(Model model) {
        return userService.getUserCityInfo(1, 1);
    }

    @RequestMapping("/foo")
    public String foo(Map<String, Object> model) {
        throw new RuntimeException("Foo");
    }

}
