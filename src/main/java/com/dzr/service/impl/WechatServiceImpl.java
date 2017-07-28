package com.dzr.service.impl;


import com.dzr.framework.Constant;
import com.dzr.framework.weixin.WechatUtil;
import com.dzr.mapper.primary.WechatTokenMapper;
import com.dzr.po.WechatToken;
import com.dzr.po.wx.Wechat;
import com.dzr.service.WechatService;
import com.dzr.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Service("wechatService")
@Transactional
public class WechatServiceImpl implements WechatService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WechatTokenMapper wechatTokenMapper;

    public String getAccessToken(String appid) {

        Integer nowTime = DateUtils.getNowTime();
        String token = "-1";

        Map<String, String> map = new HashMap<>();
        map.put("appid", appid);
        map.put("type", "0");
        WechatToken wechatToken = wechatTokenMapper.selectTokenByAppId(map);
        if (wechatToken != null) {
            boolean isUpdate = false;
            int timeDB = wechatToken.getUpdateTime();

            if (wechatToken.getToken() != null && !"".equals(wechatToken.getToken())) {
                if ((nowTime - timeDB) < 6000) {
                    token = wechatToken.getToken();
                    logger.info("当前使用的时间差小于6000s，取值数据库的accesstoken：" + token);
                } else {
                    Wechat accessToken = WechatUtil.getAccessToken(appid, Constant.APP_SECRET);
                    if (null != accessToken.getAccess_token() && !"".equals(accessToken.getAccess_token())) {
                        logger.info("获取到微信平台的accesstoken：" + accessToken.getAccess_token());
                        token = accessToken.getAccess_token();
                        isUpdate = true;
                    } else {
                        logger.info("微信平台获取的accessToken的结果为空");
                    }
                }
            } else {
                Wechat accessToken = WechatUtil.getAccessToken(appid, Constant.APP_SECRET);
                if (null != accessToken.getAccess_token() && !"".equals(accessToken.getAccess_token())) {
                    token = accessToken.getAccess_token();
                    logger.info("数据库中有对象但是没有token的值，获取服务器accesstoken：" + token);
                    isUpdate = true;
                } else {
                    logger.info("微信平台获取的accessToken的结果为空");
                }
            }
            //更新token和时间
            if (isUpdate) {
                updateWechat(wechatToken.getId(), token, nowTime);
            }
        } else {
            //appid对应的微信公众平台为空
            Wechat accessToken = WechatUtil.getAccessToken(Constant.APP_ID, Constant.APP_SECRET);
            if (null != accessToken.getAccess_token() && !"".equals(accessToken.getAccess_token())) {
                token = accessToken.getAccess_token();
                logger.info("数据库中没有初始值,此公众号第一次获取accesstoken: " + token);
                wechatToken.setAppid(Constant.APP_ID);
                wechatToken.setAppsecret(Constant.APP_SECRET);
                wechatToken.setName("无忧保");
                wechatToken.setType(0);
                wechatToken.setToken(token);
                wechatToken.setRemark("第一次初始化access_token");
                wechatToken.setCreateTime(nowTime);
                wechatToken.setUpdateTime(nowTime);
                wechatToken.setIsDelete(false);
                wechatTokenMapper.insertSelective(wechatToken);
            } else {
                logger.info("微信平台获取的accessToken的结果为空");
            }
        }
        return token;
    }

    /**
     * 获取用于调用微信JS接口的临时票据
     * @param appid 微信公众编号
     * @return
     */
    public String getJsTicket(String appid) {

        String ticket = "";
        Integer nowTime = DateUtils.getNowTime();
        String accessToken = this.getAccessToken(appid); //获取accessToken

        Map<String, String> map = new HashMap<>();
        map.put("appid", appid);
        map.put("type", "1");
        WechatToken jsapiTicket = wechatTokenMapper.selectTokenByAppId(map);
        if (jsapiTicket != null) {
            Boolean isUpdate = false;
            if (jsapiTicket.getToken() != null && !"".equals(jsapiTicket.getToken())) {
                int timeDB = jsapiTicket.getUpdateTime();
                if ((nowTime - timeDB) > 6000) {
                    ticket = WechatUtil.getTicket(accessToken);
                    if (!"".equals(ticket)) {
                        isUpdate = true;
                    }
                } else {
                    ticket = jsapiTicket.getToken();
                }
            } else {
                //第一次获取
                ticket = WechatUtil.getTicket(accessToken);
                if (!"".equals(ticket)) {
                    isUpdate = true;
                }
            }
            //更新token和时间
            if (isUpdate) {
                updateWechat(jsapiTicket.getId(), ticket, nowTime);
            }
        } else {
            //appid对应的微信公众平台为空
            ticket = WechatUtil.getTicket(accessToken);
            if (!"".equals(ticket)) {
                logger.info("数据库中没有初始值,此公众号第一次获取ticket: " + jsapiTicket);
                jsapiTicket.setAppid(Constant.APP_ID);
                jsapiTicket.setAppsecret(Constant.APP_SECRET);
                jsapiTicket.setName("无忧保");
                jsapiTicket.setType(1);
                jsapiTicket.setToken(ticket);
                jsapiTicket.setRemark("第一次初始化JS凭据");
                jsapiTicket.setCreateTime(nowTime);
                jsapiTicket.setUpdateTime(nowTime);
                jsapiTicket.setIsDelete(false);
                wechatTokenMapper.insertSelective(jsapiTicket);
            } else {
                logger.info("微信平台获取的accessToken的结果为空");
            }
        }
        return ticket;
    }

    /**
     * 更新值
     *
     * @param id
     * @param token
     * @param nowTime
     */
    private void updateWechat(Integer id, String token, Integer nowTime) {
        WechatToken newWechat = new WechatToken();
        newWechat.setId(id);
        newWechat.setToken(token);
        newWechat.setUpdateTime(nowTime);
        wechatTokenMapper.updateByPrimaryKeySelective(newWechat);
    }

    /**
     * 获取用于调用微信JS接口的临时票据
     * @param appid 微信公众编号
     * @return
     */
//    public Weixin getJSAPITicketIm(String appid) {
//        logger.info("accessToken失效，重新获取");
//        Weixin weixin = new Weixin();
//        String accessToken = this.getAccessTOkenIm(appid);//获取accessToken
//        JsapiTicket jsapiTicket = new JsapiTicket();
//        String ticket = "-1";
//        Date date = new Date();//创建现在的日期
//        //appid对应的微信公众平台为空
//        JSONObject jSONObject = WeixinUtil.getJSAPITicket(accessToken);
//        if (jSONObject.getInt("errcode") == 0) {
//            ticket = jSONObject.getString("ticket");
//            jsapiTicket.setTicket(ticket);
//            jsapiTicket.setCreateTime(date);
//            jsapiTicket.setAppid(appid);
//            jsapiTicketService.updateByPrimaryKeySelective(jsapiTicket);
//        }
//        weixin.setAccessToken(accessToken);
//        weixin.setJsapiTicket(ticket);
//        return weixin;
//    }

//    public String getAccessTOkenIm(String appid) {
//        String token = "-1";
//        Date date = new Date();//创建现在的日期
//        //appid对应的微信公众平台为空
//        TBWechat tBWechat = new TBWechat();
//        AccessToken accessToken = WeixinUtil.getAccessTokenForWXService(Constant.APP_ID, Constant.APP_SECRET);
//        if (null != accessToken) {
//            token = accessToken.getToken();
//            logger.info("数据库中值有误重新获取accesstoken=" + token);
//            tBWechat.setAccessToken(token);
//            tBWechat.setCreattime(date);
//            tBWechat.setAppid(Constant.APP_ID);
//            tBWechat.setAppsecret(Constant.APP_SECRET);
//            tBWechat.setWxname("无忧保");
//            tBWechatService.updateByPrimaryKeySelective(tBWechat);
//        }
//        return token;
//    }

    /**
     * 根据openid获取用户详细信息
     * @param appid  应用ID
     * @param openid
     * @return
     */
//    public JSONObject getUserInfoOfOpenId(String appid, String openid) {
//        //获取微信服务用户数据
//        Weixin weixin = this.getJSAPITicket(Constant.APP_ID);
//        String accessToken = weixin.getAccessToken();
//        logger.info("根据openid获取用户详细信息前,获取的accessToken：" + accessToken);
//
//        JSONObject jsonObject = WeixinUtil.getUserInfo(accessToken, openid);
//        if (jsonObject.toString().indexOf("access_token is invalid") > -1) {
//            weixin = this.getJSAPITicketIm(Constant.APP_ID);
//            accessToken = weixin.getAccessToken();
//            logger.info("根据openid获取用户详细信息前,立即获取的accessToken：" + accessToken);
//            jsonObject = WeixinUtil.getUserInfo(accessToken, openid);
//        }
//        logger.info("根据openid获取用户详细信息返回值：" + jsonObject);
//        return jsonObject;
//    }

    /**
     * 根据appid和openid判断当前用户是否关注公众号
     * @param appid
     * @param openid
     * @return true为已关注  false未关注
     */
//    public boolean IsSubscribe(String appid, String openid) {
//        logger.info("根据appid和openid判断当前用户是否关注公众号,appid为：" + appid + ",openid为：" + openid);
//        if (!"".equals(appid) && !"".equals(openid)) {
//            try {
//                JSONObject jSONObject = this.getUserInfoOfOpenId(appid, openid);
//                String subscribe = jSONObject.getString("subscribe");
//                logger.info("根据appid和openid判断当前用户是否关注公众号,结果为(0为未关注,1为已关注)：" + subscribe);
//                if ("1".equalsIgnoreCase(subscribe)) { //已关注
//                    return true;
//                }
//            } catch (Exception e) {
//                logger.info("根据appid和openid判断当前用户是否关注公众号出错!" + e.getMessage());
//                return false;
//            }
//        } else {
//            return false;
//        }
//        return false;
//    }

}