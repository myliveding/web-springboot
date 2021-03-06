package com.dzr.controller;

import com.dzr.framework.base.BaseController;
import com.dzr.service.BaseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author dingzr
 * @Description 不需要校验是否登录的接口
 * @ClassName FreeController
 * @since 2017/8/23 17:27
 */

@RestController
@RequestMapping("/free")
public class FreeController extends BaseController {

    @Autowired
    BaseInfoService baseInfoService;

    @RequestMapping("/sendSms")
    public Map<String, Object> sendSms(String mobile, HttpServletRequest request) {
        baseInfoService.sendSms(mobile);
        return successResult("sendSms");
    }


    @RequestMapping("/register")
    public Map<String, Object> register(String name, String mobile, String password, String code, HttpServletRequest request) {
        baseInfoService.register(name, mobile, password, code, request);
        return successResult("register");
    }


    @RequestMapping("/login")
    public Map<String, Object> login(String mobile, String password, String code, HttpServletRequest request) {
        baseInfoService.login(mobile, password, code, request);
        return successResult("login");
    }

}
