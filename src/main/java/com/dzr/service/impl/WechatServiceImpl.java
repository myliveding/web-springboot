package com.dzr.service.impl;


import com.dzr.framework.config.*;
import com.dzr.mapper.primary.WechatTokenMapper;
import com.dzr.po.WechatToken;
import com.dzr.po.wx.*;
import com.dzr.service.WechatService;
import com.dzr.util.DateUtils;
import com.dzr.util.ParseXmlUtil;
import com.dzr.util.SignUtil;
import com.dzr.util.StringUtils;
import com.dzr.weixin.common.MD5;
import com.dzr.weixin.common.ReportReqData;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.Annotations;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import net.sf.json.JSONObject;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.util.*;

@Component
@Service("wechatService")
@Transactional
public class WechatServiceImpl implements WechatService {

    private final WechatTokenMapper wechatTokenMapper;
    private final WechatParams wechatParams;
    private final TemplateConfig templateConfig;
    @Autowired
    UrlConfig urlConfig;

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
        map.put("appId", appId);
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
        map.put("appId", appId);
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
                logger.info("数据库中没有初始值,此公众号第一次获取ticket: " + ticket);
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

    /**
     * 获取页面分享信息
     *
     * @param request
     * @return
     */
    public void getWechatShare(Model model, HttpServletRequest request) {

        String url = wechatParams.getDomain()
                + request.getContextPath() // 项目名称
                + request.getServletPath(); // 请求页面或其他地址
//        String url = request.getScheme()
//                + "://" + request.getServerName() // 服务器地址
//                + ":" + request.getServerPort()
//                + request.getContextPath() // 项目名称
//                + request.getServletPath(); // 请求页面或其他地址
        if (StringUtils.isNotEmpty(request.getQueryString())) {
            url = url + "?" + (request.getQueryString()); //url后面的参数
        }
        logger.info("JS调用时的确切路径，需要在加密时使用：" + url); // 当前网页的URL，不包含#及其后面部分
        try {
            String jsTicket = getJsTicket(wechatParams.getAppId());
            logger.info("jsapi_ticket:" + jsTicket);
            model.addAttribute("ticket", jsTicket);
            String signature = SignUtil.getSignature(jsTicket,
                    wechatParams.getTimeStamp(), wechatParams.getNoncestr(), url);
            logger.info("signature:" + signature);
            model.addAttribute("signature", signature);
        } catch (Exception e) {
            logger.error("调用去获取分享相关信息出错..." + e.getMessage());
            e.printStackTrace();
        }
        model.addAttribute("timestamp", wechatParams.getTimeStamp());
        model.addAttribute("noncestr", wechatParams.getNoncestr());
        model.addAttribute("appid", wechatParams.getAppId());
    }

    public String wechatPay(HttpServletRequest req, Model model) {
        HttpSession session = req.getSession();
        Object openidObj = session.getAttribute("openid");
        String openid = openidObj.toString();
        logger.info("支付调用时获取的openid为：" + openid);
        if (null != openid && !"".equals(openid)) {
            String productId = req.getParameter("productId");
            if (StringUtils.isEmpty(productId)) {
                req.setAttribute("error", "商品不能为空");
                return "error";
            }
            //判断订单金额
            String productName = "臻品珠宝支付订单";
            String totalFee;
            double orderAmt = Double.valueOf(req.getParameter("price"));
            String payAmt = new java.text.DecimalFormat("#0.00").format(orderAmt);
            logger.info("ordermoney（元）:" + payAmt);
            model.addAttribute("orderMoney", payAmt);
            String type = req.getParameter("type");
            if (StringUtils.isEmpty(type)) {
                req.setAttribute("error", "支付来源不能为空");
                return "error";
            }
            model.addAttribute("type", type);
            model.addAttribute("sumAmt", payAmt);
            if (!type.equals("1")) {
                model.addAttribute("balanceAmt", payAmt);
                model.addAttribute("integral", payAmt);
                model.addAttribute("discount", payAmt);
            }

            if (StringUtils.isEmpty(payAmt)) {
                payAmt = "0";
            } else {
                int index = payAmt.indexOf(".");
                int length = payAmt.length();
                Long amLong = 0l;
                if (index == -1) {
                    amLong = Long.valueOf(payAmt + "00");
                } else if (length - index >= 3) {
                    amLong = Long.valueOf((payAmt.substring(0, index + 3)).replace(".", ""));
                } else if (length - index == 2) {
                    amLong = Long.valueOf((payAmt.substring(0, index + 2)).replace(".", "") + 0);
                } else {
                    amLong = Long.valueOf((payAmt.substring(0, index + 1)).replace(".", "") + "00");
                }
                payAmt = amLong + "";
                logger.info("order_money（分）:" + payAmt);
            }
            totalFee = payAmt;
            //调用微信支付
            try {
                String nonceStr = RandomStringUtils.random(30, "123456789qwertyuioplkjhgfdsazxcvbnm"); // 8位随机数
                String orderNo = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);

                ReportReqData reportReqData = new ReportReqData(productId, productName, openid, orderNo, getIp(req), totalFee, nonceStr);
                XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
                Annotations.configureAliases(xStreamForRequestPostData, ReportReqData.class);
                String postDataXML = xStreamForRequestPostData.toXML(reportReqData);
                logger.info("wechat send postDataXML：" + postDataXML);
                String jsonObject = WechatUtil.httpRequest("https://api.mch.weixin.qq.com/pay/unifiedorder", "POST", postDataXML);
                logger.info("wechat return jsonObject：" + jsonObject);
                Map<String, String> map = ParseXmlUtil.parseXmlText(jsonObject);
                String prepayId = map.get("prepay_id");
                Map<String, String> paymap = new HashMap<>();
                String paytimeStamp = new Date().getTime() + "";
                String paynonceStr = RandomStringUtils.random(30, "123456789qwertyuioplkjhgfdsazxcvbnm");
                paymap.put("appId", wechatParams.getAppId());
                paymap.put("timeStamp", paytimeStamp);
                paymap.put("nonceStr", paynonceStr);
                paymap.put("package", "prepay_id=" + prepayId);
                paymap.put("signType", "MD5");
                String pay2sign = getSign(paymap);
                model.addAttribute("appid", wechatParams.getAppId());
                model.addAttribute("timeStamp", paytimeStamp);
                model.addAttribute("nonceStr", paynonceStr);
                model.addAttribute("_package", "prepay_id=" + prepayId);
                model.addAttribute("paySign", pay2sign);
                logger.info("下一步返回页面..." + prepayId);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return "pay";
    }

    public String getSign(Map<String, String> map) {
        ArrayList<String> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue() != "") {
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + wechatParams.getKey();
        result = MD5.MD5Encode(result).toUpperCase();
        return result;
    }

    /**
     * 获取ip
     *
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request) {
        if (request == null)
            return "";
        String ip = request.getHeader("X-Requested-For");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    public void wechatNotify(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("text/xml");
            Map<String, String> requestMap = MessageUtil.parseXml(request);
            String returnCode = requestMap.get("return_code");
            String returnMsg = requestMap.get("return_msg");
            logger.info("return_code:" + returnCode);
            logger.info("return_msg:" + returnMsg);
            String serialNo = ""; // 交易流水号
            String orderNo = ""; // 订单号
            String openid = "";
            String productId = "";
            String thirdPayAmt = "0";//订单金额
            if ("SUCCESS".equals(returnCode)) {
                for (String key : requestMap.keySet()) {
                    logger.info("key=------- " + key + " and value=---- " + requestMap.get(key));
                    if ("transaction_id".equals(key)) {
                        serialNo = requestMap.get(key);
                    } else if ("out_trade_no".equals(key)) {
                        orderNo = requestMap.get(key);
                    } else if ("openid".equals(key)) {
                        openid = requestMap.get(key);
                    } else if ("attach".equals(key)) {
                        productId = requestMap.get(key);
                    } else if ("total_fee".equals(key)) {
                        thirdPayAmt = requestMap.get(key);
                        try {
                            StringBuffer result = new StringBuffer();
                            if (thirdPayAmt.length() == 1) {
                                result.append("0.0").append(thirdPayAmt);
                            } else if (thirdPayAmt.length() == 2) {
                                result.append("0.").append(thirdPayAmt);
                            } else {
                                String intString = thirdPayAmt.substring(0, thirdPayAmt.length() - 2);
                                for (int i = 1; i <= intString.length(); i++) {
                                    if ((i - 1) % 3 == 0 && i != 1) {
                                        result.append(",");
                                    }
                                    result.append(intString.substring(intString.length() - i, intString.length() - i + 1));
                                }
                                result.reverse().append(".").append(thirdPayAmt.substring(thirdPayAmt.length() - 2));
                            }
                            thirdPayAmt = result.toString();

                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                }
                logger.info("交易流水号serialNo:" + serialNo);
                logger.info("商品ID:" + productId);
                logger.info("订单号orderNo:" + orderNo);
                logger.info("订单金额payAmt:" + thirdPayAmt);
                logger.info("支付回调用时获取的openid为：" + openid);


                //订单业务
                if (StringUtils.isNotEmpty(orderNo)) {
                    if (!"".equals(openid)) {
                        String[] arr = new String[]{"open_id" + openid, "member_recharge_id" + productId, "serial_no" + serialNo};
                        String mystr = "open_id=" + openid + "&member_recharge_id=" + productId + "&serial_no=" + serialNo;

                        JSONObject resultStr = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.RECHARGE_SUC, mystr, arr));
                        if (resultStr.containsKey("error_code") && 0 == resultStr.getInt("error_code")) {
                            logger.info("微信支付确认订单完成:" + orderNo);
                            String resXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml> ";
                            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
                            bufferedOutputStream.write(resXml.getBytes());
                            bufferedOutputStream.flush();
                            bufferedOutputStream.close();
                        } else {
                            response.getWriter().println("<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[支付发生异常，请联系无忧保客服400-111-8900]]></return_msg></xml>");//支付成功，确认清单失败的处理
                        }
                    } else {
                        logger.info("根据openid获取的用户对象为空");
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}