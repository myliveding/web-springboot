package com.dzr.service;

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
}
