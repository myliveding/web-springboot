package com.dzr.controller;

import com.dzr.service.BaseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description
 * @FileName IndexController
 * @Author dingzr
 * @CreateTime 2017/8/20 22:26 八月
 */

@Controller
public class IndexController {

    @Autowired
    BaseInfoService baseInfoService;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("banners", baseInfoService.getBanners());
        return "bshow";
    }
}
