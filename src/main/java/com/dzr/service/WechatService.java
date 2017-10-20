package com.dzr.service;

import com.dzr.po.WechatShare;
import com.dzr.po.wx.WechatUser;
import net.sf.json.JSONObject;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

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

    /**
     * 发送模板消息
     *
     * @return
     */
    JSONObject sendTemplateMessage(String openId, String remarkStr, String url,
                                   String firstStr, String... keywordStr);

    /**
     * 获取页面分享信息
     *
     * @param request
     * @return
     */
    void getWechatShare(Model model, HttpServletRequest request);

}
