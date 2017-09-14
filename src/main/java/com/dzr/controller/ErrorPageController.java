package com.dzr.controller;

import com.dzr.framework.config.UrlConfig;
import com.dzr.framework.config.WechatParams;
import com.dzr.service.BaseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author dingzr
 * @Description 异常处理控制器
 * @ClassName ErrorPageController
 * @since 2017/8/23 15:44
 */

@Controller
public class ErrorPageController implements ErrorController {

    @Autowired
    UrlConfig urlConfig;
    @Autowired
    WechatParams wechatParams;

    /**
     * 项目的默认首页
     *
     * @param request
     * @return
     */
    @RequestMapping("/")
    public String index(HttpServletRequest request) {

        String userId = (String) request.getSession().getAttribute("userId");
        if (null == userId || "".equals(userId)) {
            return "login";
        } else {
            return "redirect:" + wechatParams.getDomain() + "/login/index";
        }
//        return "index";
    }


    private static final String ERROR_PATH = "/error";

    @RequestMapping(value = ERROR_PATH)
    public String handleError() {
        return "error";
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

}
