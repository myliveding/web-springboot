package com.dzr.service.impl;


import com.dzr.framework.config.TemplateConfig;
import com.dzr.framework.config.WechatParams;
import com.dzr.framework.config.WechatUtil;
import com.dzr.mapper.primary.WechatTokenMapper;
import com.dzr.po.WechatToken;
import com.dzr.po.wx.TemplateData;
import com.dzr.po.wx.Wechat;
import com.dzr.po.wx.WechatUser;
import com.dzr.po.wx.Template;
import com.dzr.service.WechatService;
import com.dzr.util.DateUtils;
import com.dzr.util.StringUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Component
@Service("wechatService")
@Transactional
public class WechatServiceImpl implements WechatService {

    private final WechatTokenMapper wechatTokenMapper;
    private final WechatParams wechatParams;
    private final TemplateConfig templateConfig;

    @Autowired
    public WechatServiceImpl(WechatParams wechatParams, WechatTokenMapper wechatTokenMapper, TemplateConfig templateConfig) {
        this.wechatParams = wechatParams;
        this.wechatTokenMapper = wechatTokenMapper;
        this.templateConfig = templateConfig;
    }

    private Logger logger = LoggerFactory.getLogger(WechatServiceImpl.class);

    /**
     * 获取微信公众号的access_token
     *
     * @param appId
     */
    public String getAccessToken(String appId) {

        Integer nowTime = DateUtils.getNowTime();
        String token = "-1";

        Map<String, String> map = new HashMap<>();
        map.put("appid", appId);
        map.put("type", "0");
        WechatToken wechatToken = wechatTokenMapper.selectTokenByAppId(map);
        if (wechatToken != null) {
            boolean isUpdate = false;
            int timeDB = wechatToken.getUpdateTime();

            if (wechatToken.getToken() != null && !"".equals(wechatToken.getToken())) {
                if ((nowTime - timeDB) < 6000) {
                    token = wechatToken.getToken();
                    logger.info("当前使用的时间差小于6000s，取值数据库的AccessToken：" + token);
                } else {
                    Wechat accessToken = WechatUtil.getAccessToken(appId, wechatParams.getAppSecret());
                    if (null != accessToken.getAccess_token() && !"".equals(accessToken.getAccess_token())) {
                        logger.info("获取到微信平台的AccessToken：" + accessToken.getAccess_token());
                        token = accessToken.getAccess_token();
                        isUpdate = true;
                    } else {
                        logger.info("微信平台获取的accessToken的结果为空");
                    }
                }
            } else {
                Wechat accessToken = WechatUtil.getAccessToken(appId, wechatParams.getAppSecret());
                if (null != accessToken.getAccess_token() && !"".equals(accessToken.getAccess_token())) {
                    token = accessToken.getAccess_token();
                    logger.info("数据库中有对象但是没有token的值，获取服务器AccessToken：" + token);
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
            Wechat accessToken = WechatUtil.getAccessToken(wechatParams.getAppId(), wechatParams.getAppSecret());
            if (null != accessToken.getAccess_token()
                    && !"".equals(accessToken.getAccess_token())) {
                token = accessToken.getAccess_token();
                logger.info("数据库中没有初始值,此公众号第一次获取AccessToken: " + token);
                wechatToken.setAppId(wechatParams.getAppId());
                wechatToken.setAppSecret(wechatParams.getAppSecret());
                wechatToken.setName("臻品");
                wechatToken.setType(0);
                wechatToken.setToken(token);
                wechatToken.setRemark("初始化AccessToken");
                wechatToken.setCreateTime(nowTime);
                wechatToken.setUpdateTime(nowTime);
                wechatToken.setIsDelete(false);
                wechatTokenMapper.insertSelective(wechatToken);
            } else {
                logger.error("微信平台获取的AccessToken的结果为空");
            }
        }
        return token;
    }

    /**
     * 获取用于调用微信JS接口的临时票据
     * @param appId 微信公众编号
     */
    public String getJsTicket(String appId) {

        String ticket;
        Integer nowTime = DateUtils.getNowTime();
        String accessToken = this.getAccessToken(appId);

        Map<String, String> map = new HashMap<>();
        map.put("appid", appId);
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
                jsapiTicket.setAppId(wechatParams.getAppId());
                jsapiTicket.setAppSecret(wechatParams.getAppSecret());
                jsapiTicket.setName("臻品");
                jsapiTicket.setType(1);
                jsapiTicket.setToken(ticket);
                jsapiTicket.setRemark("初始化jsapiTicket");
                jsapiTicket.setCreateTime(nowTime);
                jsapiTicket.setUpdateTime(nowTime);
                jsapiTicket.setIsDelete(false);
                wechatTokenMapper.insertSelective(jsapiTicket);
            } else {
                logger.info("微信平台获取的jsapiTicket结果为空");
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
     * @param openid
     * @return
     */
    public WechatUser getUserInfo(String openid) {
        //获取微信服务用户数据
        String accessToken = this.getAccessToken(wechatParams.getAppId());
        logger.info("根据openid获取用户详细信息前,获取的accessToken：" + accessToken);

        WechatUser userInfo = WechatUtil.getUserInfo(accessToken, openid);
        if (null != userInfo.getErrmsg() && userInfo.getErrmsg().contains("access_token is invalid")) {
            accessToken = this.getAccessTokenForError(wechatParams.getAppId());
            logger.info("根据openid获取用户详细信息前,立即获取的accessToken：" + accessToken);
            userInfo = WechatUtil.getUserInfo(accessToken, openid);
        }
        logger.info("根据openid获取用户详细信息返回值：" + userInfo.getSubscribe());
        return userInfo;
    }

    /**
     * 根据appid和openid判断当前用户是否关注公众号
     * @param openid
     * @return true为已关注  false未关注
     */
    public boolean isSubscribe(String openid) {
        logger.info("根据appid和openid判断当前用户是否关注公众号,openid为：" + openid);
        if (!"".equals(openid)) {
            WechatUser wechatUser = this.getUserInfo(openid);
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
     * @param appId
     * @return
     */
    public String getAccessTokenForError(String appId) {
        String token = "-1";
        Integer nowTime = DateUtils.getNowTime();
        //appid对应的微信公众平台为空
        Wechat accessToken = WechatUtil.getAccessToken(wechatParams.getAppId(), wechatParams.getAppSecret());
        if (null != accessToken) {
            token = accessToken.getAccess_token();
            logger.info("数据库中值有误重新获取AccessToken：" + token);
            updateByAppId(token, nowTime);
        }
        return token;
    }

    /**
     * 把获取的最新Access_token存入数据库
     * @param token
     * @param nowTime
     */
    private void updateByAppId(String token, Integer nowTime) {
        WechatToken wechatToken = new WechatToken();
        wechatToken.setAppId(wechatParams.getAppId());
        wechatToken.setType(0);
        wechatToken.setToken(token);
        wechatToken.setUpdateTime(nowTime);
        wechatToken.setRemark("数据库中值有误重新获取AccessToken");
        wechatTokenMapper.updateByAppId(wechatToken);
    }

    /**
     * 发送模板消息
     * @param openId
     * @param remarkStr
     * @param url 跳转的路径
     * @param firstStr 第一个字段
     * @param keywordStr 发送的关键字
     * @return
     */
    @Override
    public JSONObject sendTemplateMessage(String openId, String remarkStr, String url,
                                          String firstStr, String... keywordStr) {
        Template template = new Template();

        if (StringUtils.isNotEmpty(url)) {
            template.setUrl(url);
        }
        template.setTouser(openId);
        template.setColor("#0000FF");
        template.setTemplate_id(templateConfig.getBuySuss());

        Map<String, TemplateData> mData = new HashMap<>();
        TemplateData first = new TemplateData();
        first.setColor("#6397A9");
        first.setValue(firstStr + "\n");
        mData.put("first", first);

        if (StringUtils.isNotEmpty(keywordStr[0])) {
            TemplateData keynote1 = new TemplateData();
            keynote1.setColor("#6397A9");
            keynote1.setValue(keywordStr[0]);
            mData.put("keynote1", keynote1);
        }

        if (StringUtils.isNotEmpty(keywordStr[1])) {
            TemplateData keynote2 = new TemplateData();
            keynote2.setColor("#6397A9");
            keynote2.setValue(keywordStr[1]);
            mData.put("keynote2", keynote2);
        }

        if (StringUtils.isNotEmpty(remarkStr)) {
            TemplateData remark = new TemplateData();
            remark.setColor("#6397A9");
            remark.setValue(remarkStr);
            mData.put("remark", remark);
        }
        template.setData(mData);

        String content = JSONObject.fromObject(template).toString();
        logger.info("发送消息" + content);
        String accessToken = getAccessToken(wechatParams.getAppId());
        JSONObject jsonObject = JSONObject.fromObject(WechatUtil.sendTemplateMessage(accessToken, content));
        if ((jsonObject != null) && (jsonObject.getInt("errcode") != 0)) {
            if (jsonObject.getInt("errcode") == 40001) {
                accessToken = getAccessToken(wechatParams.getAppId());
                jsonObject = JSONObject.fromObject(WechatUtil.sendTemplateMessage(accessToken, content));
            }
        }
        if (null != jsonObject) {
            logger.info("发送消息结果  " + jsonObject.toString());
        } else {
            logger.info("发送消息结果 null");
        }
        return jsonObject;
    }
}