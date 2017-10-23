package com.dzr.controller;

import com.dzr.framework.config.UrlConfig;
import com.dzr.framework.config.WechatParams;
import com.dzr.util.StringUtils;
import org.apache.log4j.Logger;
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

    private Logger logger = Logger.getLogger(ErrorPageController.class);
    /**
     * 项目的默认首页
     *
     * @param request
     * @return
     */
    @RequestMapping("/")
    public String index(HttpServletRequest request, Model model) {

        String userId = (String) request.getSession().getAttribute("userId");
        String backUrl = (String) request.getSession().getAttribute("backUrl");
        logger.info("backUrl--" + backUrl);
        if (null != backUrl && StringUtils.isNotEmpty(backUrl)) {
            model.addAttribute("backUrl", backUrl);
            // /login/receiveCardPage?telphone=11111&cardId=99
            //substring 从0开始计数 左闭右开
            model.addAttribute("tel", backUrl.substring(32, 43));
        }
        if (null == userId || "".equals(userId)) {
            return "login";
        } else {
            return "redirect:" + wechatParams.getDomain() + "/login/index";
        }
    }


    @RequestMapping("/error")
    public String handleError() {
        return "error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

}
