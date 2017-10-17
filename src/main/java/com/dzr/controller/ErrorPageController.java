package com.dzr.controller;

import com.dzr.framework.config.UrlConfig;
import com.dzr.framework.config.WechatParams;
import com.dzr.service.BaseInfoService;
import com.dzr.service.impl.CoreServiceImpl;
import com.dzr.util.SignUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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


    private Logger logger = Logger.getLogger(CoreAction.class);
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


    /**
     * 确认请求来自微信服务器
     */
    @RequestMapping("/doGet")
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");
        logger.info("signature--" + signature + "--timestamp--" + timestamp
                + "--nonce--" + nonce + "--echostr--" + echostr);

        PrintWriter out = response.getWriter();
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        if (SignUtil.checkSignature(signature, timestamp, nonce)) {
            out.print(echostr);
        }
        out.close();
        out = null;
    }

    /**
     * 处理微信服务器发来的消息
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 调用核心业务类接收消息、处理消息
        String respMessage = CoreServiceImpl.processRequest(request);

        // 响应消息
        PrintWriter out = response.getWriter();
        out.print(respMessage);
        out.close();
    }

}
