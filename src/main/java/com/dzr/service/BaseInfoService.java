package com.dzr.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description
 * @FileName BaseInfoService
 * @Author dingzr
 * @CreateTime 2017/8/21 22:22 八月
 */
public interface BaseInfoService {

    /**
     * 验证码
     *
     * @param mobile
     */
    void sendSms(String mobile);

    /**
     * 注册方法
     *
     * @param mobile
     */
    void register(String name, String birth, String mobile, String password, String code, HttpServletRequest request);

    JSONArray getBanners();

    /**
     * 获取公司协议
     *
     * @return
     */
    void getCompanyProtocol();

    /**
     * 登录方法
     *
     * @param mobile
     * @param password
     * @param code
     */
    void login(String mobile, String password, String code, HttpServletRequest request);

    /**
     * 获取PHP的用户信息
     *
     * @param memberId
     * @return
     */
    JSONObject getUserInfo(String memberId);

}
