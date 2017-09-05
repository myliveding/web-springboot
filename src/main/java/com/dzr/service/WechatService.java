package com.dzr.service;

import com.dzr.po.wx.WechatUser;

/**
 * @author dingzr
 * @Description
 * @ClassName WechatService
 * @since 2017/7/28 15:45
 */
public interface WechatService {

    /**
     * 根据openid获取用户详细信息
     *
     * @param openid
     * @return
     */
    WechatUser getUserInfo(String openid);

}
