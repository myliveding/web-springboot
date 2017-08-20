package com.dzr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author dingzr
 * @Description 默认进去的后缀名称为templates目录下html的文件
 * @ClassName UserHtmlController
 * @since 2017/7/4 15:57
 */

@Controller
@RequestMapping("/userHtml")
public class UserHtmlController {

    @RequestMapping("/{name}")
    public String hello(@PathVariable("name") String name, Model model) {
        model.addAttribute("name", name);
        return "myonly/hello";
    }

    @RequestMapping("/more")
    public String more(){
        return "myonly/hello2";
    }

    @RequestMapping("/map")
    public String map() {
        return "myonly/map";
    }

    @RequestMapping("/mapDemo")
    public String mapDemo() {
        return "myonly/mapDemo";
    }

    @RequestMapping("/main")
    public String main() {
        return "myonly/allmap";
    }


}
