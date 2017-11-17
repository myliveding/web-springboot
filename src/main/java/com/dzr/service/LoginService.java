package com.dzr.service;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description
 * @FileName LoginService
 * @Author dingzr
 * @CreateTime 2017/8/21 22:22 八月
 */
public interface LoginService {

    String gotoVip(Model model, HttpServletRequest request);

    String gotoCode(Model model, HttpServletRequest request);

    String gotoDiscountCard(Model model, HttpServletRequest request);

    void receiveCard(Model model, HttpServletRequest request);

    /**
     * 扫码支付的最优选择
     *
     * @param money
     * @param request
     */
    JSONObject payChoice(String money, HttpServletRequest request);
}
