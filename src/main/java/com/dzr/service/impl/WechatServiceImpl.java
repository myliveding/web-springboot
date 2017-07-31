package com.dzr.service.impl;


import com.dzr.framework.Constant;
import com.dzr.framework.weixin.WechatUtil;
import com.dzr.mapper.primary.WechatTokenMapper;
import com.dzr.po.WechatToken;
import com.dzr.po.wx.Wechat;
import com.dzr.po.wx.WechatUser;
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
     * 根据openid获取用户详细信息
     * @param appid  应用ID
     * @param openid
     * @return
     */
    public WechatUser getUserInfo(String appid, String openid) {
        //获取微信服务用户数据
        String accessToken = this.getAccessToken(Constant.APP_ID);
        logger.info("根据openid获取用户详细信息前,获取的accessToken：" + accessToken);

        WechatUser userInfo = WechatUtil.getUserInfo(accessToken, openid);
        if (null != userInfo.getErrmsg() && userInfo.getErrmsg().indexOf("access_token is invalid") > -1) {
            accessToken = this.getAccessTokenForError(Constant.APP_ID);
            logger.info("根据openid获取用户详细信息前,立即获取的accessToken：" + accessToken);
            userInfo = WechatUtil.getUserInfo(accessToken, openid);
        }
        logger.info("根据openid获取用户详细信息返回值：" + userInfo.getSubscribe());
        return userInfo;
    }

    /**
     * 根据appid和openid判断当前用户是否关注公众号
     * @param appid
     * @param openid
     * @return true为已关注  false未关注
     */
    public boolean isSubscribe(String appid, String openid) {
        logger.info("根据appid和openid判断当前用户是否关注公众号,appid为：" + appid + ",openid为：" + openid);
        if (!"".equals(appid) && !"".equals(openid)) {
            WechatUser wechatUser = this.getUserInfo(appid, openid);
            if (null != wechatUser.getSubscribe()) {
                String subscribe = wechatUser.getSubscribe();
                logger.info("根据appid和openid判断当前用户是否关注公众号,结果为(0为未关注,1为已关注)：" + subscribe);
                if ("1".equalsIgnoreCase(subscribe)) { //已关注
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * AccessToken错误重新获取
     *
     * @param appid
     * @return
     */
    public String getAccessTokenForError(String appid) {
        String token = "-1";
        Integer nowTime = DateUtils.getNowTime();
        //appid对应的微信公众平台为空
        Wechat accessToken = WechatUtil.getAccessToken(Constant.APP_ID, Constant.APP_SECRET);
        if (null != accessToken) {
            token = accessToken.getAccess_token();
            logger.info("数据库中值有误重新获取accesstoken：" + token);
            updateByAppId(0, token, "数据库中值有误重新获取accesstoken", nowTime);
            logger.error("数据库中值有误重新获取accesstoken：" + token);
        }
        return token;
    }

    /**
     * 把左心呃token存入数据库
     *
     * @param type
     * @param token
     * @param remark
     * @param nowTime
     */
    private void updateByAppId(Integer type, String token, String remark, Integer nowTime) {
        WechatToken wechatToken = new WechatToken();
        wechatToken.setAppid(Constant.APP_ID);
        wechatToken.setType(type);
        wechatToken.setToken(token);
        wechatToken.setUpdateTime(nowTime);
        wechatToken.setRemark(remark);
        wechatTokenMapper.updateByAppId(wechatToken);
    }

}