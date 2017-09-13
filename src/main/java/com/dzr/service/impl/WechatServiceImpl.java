package com.dzr.service.impl;


import com.dzr.framework.config.WechatParams;
import com.dzr.framework.config.WechatUtil;
import com.dzr.mapper.primary.WechatTokenMapper;
import com.dzr.po.WechatToken;
import com.dzr.po.wx.Wechat;
import com.dzr.po.wx.WechatUser;
import com.dzr.service.WechatService;
import com.dzr.util.DateUtils;
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

    @Autowired
    public WechatServiceImpl(WechatParams wechatParams, WechatTokenMapper wechatTokenMapper) {
        this.wechatParams = wechatParams;
        this.wechatTokenMapper = wechatTokenMapper;
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
     *
     * @param type 待发送的消息
     * @return
     */
    public JSONObject sendTemplateMessageByType(String type, String firstStr, String keyword1Str, String keyword2Str, String keyword3Str, String keyword4Str, String keyword5Str, String openId, String remarkStr, String url) {
//     String type=request.getParameter("type"); //0 业务服务提醒 ；  1 认证通知；2 消息提醒 ；3 获得代金券通知；4注册通知；
//5 参保成功通知；6参保失败通知；7停保成功通知；8停保失败通知；9退款成功通知；10服务到期提醒；11业务办理取消通知；12订单未支付通知;13订单支付成功；14业务动态提醒；15手机号绑定提醒
        WxTemplate t = new WxTemplate();
        if (StringUtils.isEmpty(remarkStr)) {
            if (type.equals("2")) {
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
        if (type.equals("0")) {//业务服务提醒
            t.setTemplate_id(Constant.TEMPLATE_SERVICE);
            m.put("keyword1", keyword1);
            m.put("keyword2", keyword2);
            m.put("keyword3", keyword3);
        }
        if (type.equals("1")) {//认证通知
            t.setTemplate_id(Constant.TEMPLATE_AUTHENTICATION);
            m.put("keyword1", keyword1);
            m.put("keyword2", keyword2);
        }
        if (type.equals("2")) {//消息提醒
            t.setTemplate_id(Constant.TEMPLATE_REMIND);
            m.put("keyword1", keyword1);
            m.put("keyword2", keyword2);
        }
        if (type.equals("3")) {//获得代金券通知
            t.setTemplate_id(Constant.TEMPLATE_VOUCHER);
            m.put("coupon", keyword1);
            m.put("expDate", keyword2);
        }
        if (type.equals("4")) {//注册通知
            t.setTemplate_id(Constant.TEMPLATE_REGISTERED);
            m.put("keyword1", keyword1);
            m.put("keyword2", keyword2);
        }
        if (type.equals("5")) {//参保成功通知
            t.setTemplate_id(Constant.TEMPLATE_SERVICE_SUCC);
            m.put("keyword1", keyword1);
            m.put("keyword2", keyword2);
            m.put("keyword3", keyword3);
            m.put("keyword4", keyword4);
        }
        if (type.equals("6")) {//参保失败通知
            t.setTemplate_id(Constant.TEMPLATE_SERVICE_FAIL);
            m.put("keyword1", keyword1);
            m.put("keyword2", keyword2);
            m.put("keyword3", keyword3);
        }
        if (type.equals("7")) {//停保成功通知
            t.setTemplate_id(Constant.TEMPLATE_STOP_SUCC);
            m.put("keyword1", keyword1);
            m.put("keyword2", keyword2);
            m.put("keyword3", keyword3);
            m.put("keyword4", keyword4);
        }
        if (type.equals("8")) {//停保失败通知
            t.setTemplate_id(Constant.TEMPLATE_STOP_FAIL);
            m.put("keyword1", keyword1);
            m.put("keyword2", keyword2);
            m.put("keyword3", keyword3);
            m.put("keyword4", keyword4);
        }
        if (type.equals("9")) {//退款成功通知
            t.setTemplate_id(Constant.TEMPLATE_REFUND_SUCC);
            m.put("keyword1", keyword1);
            m.put("keyword2", keyword2);
            m.put("keyword3", keyword3);
        }
        if (type.equals("10")) {//续保提醒
            t.setTemplate_id(Constant.TEMPLATE_SERVICE_EXPIRE);
            m.put("keyword1", keyword1);
            m.put("keyword2", keyword2);
            m.put("keyword3", keyword3);
        }
        if (type.equals("11")) {//业务办理取消通知
            t.setTemplate_id(Constant.TEMPLATE_DEAL_CANCEL);
            m.put("keyword1", keyword1);
            m.put("keyword2", keyword2);
            m.put("keyword3", keyword3);
        }
        if (type.equals("12")) {//订单未支付通知v2
            t.setTemplate_id(Constant.TEMPLATE_UNPAY);
            m.put("type", keyword1);
            m.put("e_title", keyword2);
            m.put("o_id", keyword3);
            m.put("order_date", keyword4);
            m.put("o_money", keyword5);
        }
        if (type.equals("13")) {//订单支付成功
            t.setTemplate_id(Constant.TEMPLATE_PAY_SUCC);
            m.put("orderMoneySum", keyword1);
            m.put("orderProductName", keyword2);
            m.put("Remark", remark);
        }
        if (type.equals("14")) {//业务动态提醒
            t.setTemplate_id(Constant.TEMPLATE_BUS_DYNAMICS);
            m.put("keyword1", keyword1);
            m.put("keyword2", keyword2);
        }
        if (type.equals("15")) {//手机号绑定提醒
            t.setTemplate_id(Constant.TEMPLATE_BUILD_MOBILE);
            m.put("keyword1", keyword1);
            m.put("keyword2", keyword2);
        }
        if (type.equals("16")) {//参保材料通知
            t.setTemplate_id(Constant.TEMPLATE_INSURED_MATERIAL);
            m.put("keyword1", keyword1);
            m.put("keyword2", keyword2);
            m.put("keyword3", keyword3);
            m.put("keyword4", keyword4);
        }
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
}