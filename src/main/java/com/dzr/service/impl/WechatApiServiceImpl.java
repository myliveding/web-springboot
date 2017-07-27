package com.dzr.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;


@Component
public class WechatApiServiceImpl {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private TBWechatService tBWechatService;

    @Resource
    private JsapiTicketService jsapiTicketService;


    public String getAccessTOken(String appid) {
        TBWechat tBWechat = tBWechatService.selectByPrimaryKey(appid);
        String token = "-1";
        Date date = new Date();//创建现在的日期
        if (!"".equals(tBWechat) && tBWechat != null) {
            long timeDB = tBWechat.getCreattime().getTime();
            long timeService = date.getTime();//获得毫秒数
            if (!"".equals(tBWechat.getAccessToken()) && tBWechat.getAccessToken() != null) {
                if ((timeService - timeDB) < 5000000) {
                    token = tBWechat.getAccessToken();
                    logger.info("获取到数据库accesstoken：" + token);
                } else {
                    String appsecret = tBWechat.getAppsecret();
                    AccessToken accessToken = WeixinUtil.getAccessTokenForWXService(appid, appsecret);
                    if (null != accessToken) {
                        logger.info("获取到服务器的accesstoken：" + accessToken.getToken());
                        token = accessToken.getToken();
                        tBWechat.setAccessToken(accessToken.getToken());
                        tBWechat.setCreattime(date);
                        tBWechatService.updateByPrimaryKeySelective(tBWechat);
                    }
                }
            } else {
                String appsecret = tBWechat.getAppsecret();
                AccessToken accessToken = WeixinUtil.getAccessTokenForWXService(appid, appsecret);
                if (null != accessToken) {
                    token = accessToken.getToken();
                    logger.info("数据库中有值时,此公众号第一次获取accesstoken=" + token);
                    tBWechat.setAccessToken(accessToken.getToken());
                    tBWechat.setCreattime(date);
                    tBWechatService.updateByPrimaryKeySelective(tBWechat);
                }
            }


        } else {
            //appid对应的微信公众平台为空
            tBWechat = new TBWechat();
            AccessToken accessToken = WeixinUtil.getAccessTokenForWXService(Constant.APP_ID, Constant.APP_SECRET);
            if (null != accessToken) {
                token = accessToken.getToken();
                logger.info("数据库中没有初始值,此公众号第一次获取accesstoken=" + token);
                tBWechat.setAccessToken(token);
                tBWechat.setCreattime(date);
                tBWechat.setAppid(Constant.APP_ID);
                tBWechat.setAppsecret(Constant.APP_SECRET);
                tBWechat.setWxname("无忧保");
                tBWechatService.insertSelective(tBWechat);
                //	tBWechatService.updateByPrimaryKeySelective(tBWechat);
            }

        }
        return token;
    }

    /**
     * 获取用于调用微信JS接口的临时票据
     *
     * @param appid 微信公众编号
     * @return
     */
    public Weixin getJSAPITicket(String appid) {
        Weixin weixin = new Weixin();
        String accessToken = this.getAccessTOken(appid);//获取accessToken

        JsapiTicket jsapiTicket = jsapiTicketService.selectByPrimaryKey(appid);
        String ticket = "-1";
        Date date = new Date();//创建现在的日期
        if (!"".equals(jsapiTicket) && jsapiTicket != null) {
            if (!"".equals(jsapiTicket.getTicket()) && jsapiTicket.getTicket() != null) {
                long timeDB = jsapiTicket.getCreateTime().getTime();
                long timeService = date.getTime();//获得毫秒数
                if ((timeService - timeDB) > 5000000) {
                    JSONObject jSONObject = WeixinUtil.getJSAPITicket(accessToken);
                    if (jSONObject.getInt("errcode") == 0) {
                        ticket = jSONObject.getString("ticket");
                        jsapiTicket.setTicket(ticket);
                        jsapiTicket.setCreateTime(date);
                        jsapiTicketService.updateByPrimaryKeySelective(jsapiTicket);
                    }

                } else {
                    ticket = jsapiTicket.getTicket();
                }
            } else {
                //第一次获取
                JSONObject jSONObject = WeixinUtil.getJSAPITicket(accessToken);
                if (jSONObject.getInt("errcode") == 0) {
                    ticket = jSONObject.getString("ticket");
                    jsapiTicket.setTicket(ticket);
                    jsapiTicket.setCreateTime(date);
                    jsapiTicketService.updateByPrimaryKeySelective(jsapiTicket);
                }
            }
        } else {
            //appid对应的微信公众平台为空
            jsapiTicket = new JsapiTicket();
            JSONObject jSONObject = WeixinUtil.getJSAPITicket(accessToken);
            if (jSONObject.getInt("errcode") == 0) {
                ticket = jSONObject.getString("ticket");
                jsapiTicket.setTicket(ticket);
                jsapiTicket.setCreateTime(date);
                jsapiTicket.setAppid(appid);
                jsapiTicketService.insertSelective(jsapiTicket);
            }

        }
        weixin.setAccessToken(accessToken);
        weixin.setJsapiTicket(ticket);
        return weixin;
    }

    /**
     * 获取用于调用微信JS接口的临时票据
     *
     * @param appid 微信公众编号
     * @return
     */
    public Weixin getJSAPITicketIm(String appid) {
        logger.info("accessToken失效，重新获取");
        Weixin weixin = new Weixin();
        String accessToken = this.getAccessTOkenIm(appid);//获取accessToken
        JsapiTicket jsapiTicket = new JsapiTicket();
        String ticket = "-1";
        Date date = new Date();//创建现在的日期
        //appid对应的微信公众平台为空
        JSONObject jSONObject = WeixinUtil.getJSAPITicket(accessToken);
        if (jSONObject.getInt("errcode") == 0) {
            ticket = jSONObject.getString("ticket");
            jsapiTicket.setTicket(ticket);
            jsapiTicket.setCreateTime(date);
            jsapiTicket.setAppid(appid);
            jsapiTicketService.updateByPrimaryKeySelective(jsapiTicket);
        }
        weixin.setAccessToken(accessToken);
        weixin.setJsapiTicket(ticket);
        return weixin;
    }

    /**
     * 获取token
     * <p>
     * <ul>
     * <li>
     * <b>功能：<br/>
     * <p>
     * 2015-06-01<br/>
     *
     * @return token字符串 获取失败（-1）
     * </p>
     * </li>
     * </ul>
     */

    public String getAccessTOkenIm(String appid) {
        String token = "-1";
        Date date = new Date();//创建现在的日期
        //appid对应的微信公众平台为空
        TBWechat tBWechat = new TBWechat();
        AccessToken accessToken = WeixinUtil.getAccessTokenForWXService(Constant.APP_ID, Constant.APP_SECRET);
        if (null != accessToken) {
            token = accessToken.getToken();
            logger.info("数据库中值有误重新获取accesstoken=" + token);
            tBWechat.setAccessToken(token);
            tBWechat.setCreattime(date);
            tBWechat.setAppid(Constant.APP_ID);
            tBWechat.setAppsecret(Constant.APP_SECRET);
            tBWechat.setWxname("无忧保");
            tBWechatService.updateByPrimaryKeySelective(tBWechat);
        }
        return token;
    }

    /**
     * 根据openid获取用户详细信息
     *
     * @param appid  应用ID
     * @param openid
     * @return
     */
    public JSONObject getUserInfoOfOpenId(String appid, String openid) {
        //获取微信服务用户数据
        Weixin weixin = this.getJSAPITicket(Constant.APP_ID);
        String accessToken = weixin.getAccessToken();
        logger.info("根据openid获取用户详细信息前,获取的accessToken：" + accessToken);

        JSONObject jsonObject = WeixinUtil.getUserInfo(accessToken, openid);
        if (jsonObject.toString().indexOf("access_token is invalid") > -1) {
            weixin = this.getJSAPITicketIm(Constant.APP_ID);
            accessToken = weixin.getAccessToken();
            logger.info("根据openid获取用户详细信息前,立即获取的accessToken：" + accessToken);
            jsonObject = WeixinUtil.getUserInfo(accessToken, openid);
        }
        logger.info("根据openid获取用户详细信息返回值：" + jsonObject);
        return jsonObject;
    }

    /**
     * 根据appid和openid判断当前用户是否关注公众号
     *
     * @param appid
     * @param openid
     * @return true为已关注  false未关注
     */
    public boolean IsSubscribe(String appid, String openid) {
        logger.info("根据appid和openid判断当前用户是否关注公众号,appid为：" + appid + ",openid为：" + openid);
        if (DataUtil.isNotEmpty(appid) && DataUtil.isNotEmpty(openid)) {
            try {
                JSONObject jSONObject = this.getUserInfoOfOpenId(appid, openid);
                String subscribe = jSONObject.getString("subscribe");
                logger.info("根据appid和openid判断当前用户是否关注公众号,结果为(0为未关注,1为已关注)：" + subscribe);
                if ("1".equalsIgnoreCase(subscribe)) { //已关注
                    return true;
                }
            } catch (Exception e) {
                logger.info("根据appid和openid判断当前用户是否关注公众号出错!" + e.getMessage());
                return false;
            }
        } else {
            return false;
        }
        return false;
    }


    /**
     * 获取用户网页授权openid
     *
     * @param appid 应用ID
     * @param code  填写第一步获取的code参数
     * @return 获取到的openid  当 openid为 -1获取失败
     */
    public String getUserOpenIdOfScope(String appid, String code) {
        TBWechat tBWechat = tBWechatService.selectByPrimaryKey(appid);
        JSONObject jsonObject = null;
        if (null != tBWechat) {
            jsonObject = WeixinUtil.getUserInfoOfScope(appid, tBWechat.getAppsecret(), code);
        } else {
            jsonObject = WeixinUtil.getUserInfoOfScope(appid, Constant.APP_SECRET, code);
        }
        logger.info("网页授权获取用户openid的返回值：" + jsonObject.toString());
        if (jsonObject.has("openid")) {
            return jsonObject.getString("openid");
        } else {
            return "";
        }
    }

    /**
     * 获取模板ID
     *
     * @param shortId 待发送的消息标志
     * @return
     */
    public String getTemplateId(String shortId) {
        Weixin weixin = this.getJSAPITicket(Constant.APP_ID);
        String accessToken = weixin.getAccessToken();

        JSONObject jsonObject = WeixinUtil.getTemplateId(accessToken, shortId);
        if ((jsonObject != null) && (jsonObject.getInt("errcode") != 0)) {
            if (jsonObject.getInt("errcode") == 40001) {
                weixin = this.getJSAPITicketIm(Constant.APP_ID);
                accessToken = weixin.getAccessToken();
                jsonObject = WeixinUtil.getTemplateId(accessToken, shortId);
            }
        }
        return jsonObject.getString("template_id");
    }

    /**
     * 发送模板消息
     *
     * @param content 待发送的消息
     * @return
     */
    public JSONObject sendTemplateMessage(String content) {
        logger.info("发送消息" + content);
        Weixin weixin = this.getJSAPITicket(Constant.APP_ID);
        String accessToken = weixin.getAccessToken();
        JSONObject jsonObject = WeixinUtil.sendTemplateMessage(accessToken, content);
        if ((jsonObject != null) && (jsonObject.getInt("errcode") != 0)) {
            if (jsonObject.getInt("errcode") == 40001) {
                weixin = this.getJSAPITicketIm(Constant.APP_ID);
                accessToken = weixin.getAccessToken();
                jsonObject = WeixinUtil.sendTemplateMessage(accessToken, content);
            }
        }
        return jsonObject;
    }

    /**
     * 发送模板消息
     *
     * @param type 待发送的消息
     * @return
     */
    public JSONObject sendTemplateMessageByType(String type, String firstStr, String keyword1Str, String keyword2Str, String keyword3Str, String keyword4Str, String keyword5Str, String openId, String remarkStr, String url) {
//     String type=request.getParameter("type"); //0 业务服务提醒 ；  1 认证通知；2 消息提醒 ；3 获得代金券通知；4注册通知；
//5 参保成功通知；6参保失败通知；7停保成功通知；8停保失败通知；9退款成功通知；10服务到期提醒；11业务办理取消通知；12订单未支付通知;13订单支付成功；14业务动态提醒；15手机号绑定提醒
        WxTemplate t = new WxTemplate();
        if (DataUtil.isEmpty(remarkStr)) {
            if (type.equals("1") || type.equals("2")) {
                remarkStr = "感谢您对我们工作的支持！如有疑问，请拨打咨询热线400-111-8900。";
            } else if (type.equals("3")) {
                firstStr = "恭喜您获得了一张代金券！";
                remarkStr = "在参保或续费时，代金券可用于抵扣服务费，如有疑问，请拨打咨询热线400-111-8900。";
                url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + Constant.APP_ID + "&redirect_uri=" + Constant.URL + "/scope/openid.do?next=personsocial/gotovoucher.do" + Constant.APP_ID + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
            } else if (type.equals("0")) {
                remarkStr = "请在截止日前及时办理。";
            } else if (type.equals("4")) {
                firstStr = "恭喜您已成功注册为无忧保用户！";
                remarkStr = "感谢您对我们工作的支持！如有疑问，请拨打咨询热线400-111-8900。";
            }
        }
        t.setUrl(url);
        t.setTouser(openId);
        //   t.setTopcolor("#0000FF");
        Map<String, TemplateData> m = new HashMap<String, TemplateData>();
        TemplateData first = new TemplateData();
//         first.setColor("#0000FF");
        first.setValue(firstStr + "\n");
        m.put("first", first);
        TemplateData keyword1 = new TemplateData();
//         keyword1.setColor("#0000FF");
        keyword1.setValue(keyword1Str);
        TemplateData keyword2 = new TemplateData();
//         keyword2.setColor("#0000FF");
        keyword2.setValue(keyword2Str);
        TemplateData keyword3 = new TemplateData();
//         keyword3.setColor("#0000FF");
        keyword3.setValue(keyword3Str);
        TemplateData keyword4 = new TemplateData();
        keyword4.setValue(keyword4Str);
        TemplateData keyword5 = new TemplateData();
        keyword5.setValue(keyword5Str);
        TemplateData remark = new TemplateData();
//         remark.setColor("#0000FF");
        remark.setValue(remarkStr);

        m.put("remark", remark);
        t.setData(m);
        String content = JSONObject.fromObject(t).toString();
        logger.info("发送消息" + content);
        Weixin weixin = this.getJSAPITicket(Constant.APP_ID);
        String accessToken = weixin.getAccessToken();
        JSONObject jsonObject = WeixinUtil.sendTemplateMessage(accessToken, content);
        if ((jsonObject != null) && (jsonObject.getInt("errcode") != 0)) {
            if (jsonObject.getInt("errcode") == 40001) {
                weixin = this.getJSAPITicketIm(Constant.APP_ID);
                accessToken = weixin.getAccessToken();
                jsonObject = WeixinUtil.sendTemplateMessage(accessToken, content);
            }
        }
        if (null != jsonObject) {
            logger.info("发送消息结果  " + jsonObject.toString());
        } else {
            logger.info("发送消息结果 null");
        }
        return jsonObject;
    }

    /**
     * 获取用户所在分组ID
     *
     * @param openId
     * @return
     */
    public String getUserGroup(String openId) {
        Weixin weixin = this.getJSAPITicket(Constant.APP_ID);
        String accessToken = weixin.getAccessToken();
        String jsonStr = "{\"openid\":\"" + openId + "\"}";
        JSONObject jsonObject = WeixinUtil.getUserGroup(accessToken, jsonStr);
        if ((jsonObject != null) && jsonObject.containsKey("errcode")) {
            if (jsonObject.getInt("errcode") == 40001) {
                weixin = this.getJSAPITicketIm(Constant.APP_ID);
                accessToken = weixin.getAccessToken();
                jsonObject = WeixinUtil.getUserGroup(accessToken, jsonStr);
            }
        }
        if (jsonObject.containsKey("groupid")) {
            return String.valueOf(jsonObject.getInt("groupid"));
        } else {
            return "";
        }
    }

    /**
     * 从微信服务器下载多媒体文件上传到阿里云
     *
     * @param picId
     * @param accessToken
     * @param path
     * @return
     */
    public String downloadFromWxAndUploadToAliYun(String picId, String accessToken, String path) {
        String picPath = "";
        if (DataUtil.isNotEmpty(picId) && picId.indexOf("http") < 0) {
            if (WeixinUtil.checkAccessToken(accessToken, picId)) {//判断accexxTkoen的有效性，无效直接重新获取
                Weixin weixin = this.getJSAPITicketIm(Constant.APP_ID);
                accessToken = weixin.getAccessToken();
            }
            try {
                picPath = WeixinUtil.downloadMediaFromWx(accessToken, picId, path);//从微信服务器下载多媒体文件上传到阿里云
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return picPath;
    }
}