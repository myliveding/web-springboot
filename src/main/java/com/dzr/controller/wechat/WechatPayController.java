package com.dzr.controller.wechat;

import com.dzr.controller.wechat.common.MD5;
import com.dzr.controller.wechat.common.report.protocol.ReportReqData;
import com.dzr.framework.config.WechatParams;
import com.dzr.framework.config.WechatUtil;
import com.dzr.service.BaseInfoService;
import com.dzr.util.ParseXmlUtil;
import com.dzr.util.StringUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.Annotations;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import net.sf.json.JSONObject;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;


@SuppressWarnings("deprecation")
@RequestMapping("/wechatPay")
@Controller
public class WechatPayController {

    private static final Logger logger = Logger.getLogger(WechatPayController.class);

    @Autowired
    WechatParams wechatParams;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String pay(HttpServletRequest req, Model model) {

        HttpSession session = req.getSession();
        Object openidObj = session.getAttribute("openid");
        String openid = openidObj.toString();
        logger.info("支付调用时获取的openid为：" + openid);
        if (null != openid && !"".equals(openid)) {
            String userId = session.getAttribute("userId").toString();

            String orderId = req.getParameter("orderId");
            if (StringUtils.isEmpty(orderId)) {
                req.setAttribute("error", "订单不能为空");
                return "error";
            } else {
                model.addAttribute("orderId", orderId);
            }

            //判断订单金额
            String productName = "臻品会员" + orderId;
            String totalFee;
//                JSONObject message = JSONObject.fromObject(resultStr.getString("result"));
//                double orderAmt = message.getDouble("money");
            double orderAmt = 11111.22;
                String payAmt = new java.text.DecimalFormat("#0.00").format(orderAmt);
                logger.info("ordermoney（元）:" + payAmt);
                model.addAttribute("order_money", payAmt);
//                String orderStatus = message.getString("order_status");
//                if (!"1".equals(orderStatus)) {
//                    logger.info("订单状态为：" + orderStatus + "请勿重复支付");
//                    req.setAttribute("error", "请勿重复支付");
//                    return "error：请勿重复支付";
//                }
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
                ReportReqData reportReqData = new ReportReqData(productName, openid, orderId, getIp(req), totalFee, nonceStr);
                XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
                Annotations.configureAliases(xStreamForRequestPostData, ReportReqData.class);
                String postDataXML = xStreamForRequestPostData.toXML(reportReqData);
                logger.info("wechat send postDataXML：" + postDataXML);
                String jsonObject = WechatUtil.httpRequest("https://api.mch.weixin.qq.com/pay/unifiedorder", "POST", postDataXML);
                logger.info("wechat return jsonObject：" + jsonObject);
                Map<String, String> map = ParseXmlUtil.parseXmlText(jsonObject);
                String prepayId = map.get("prepay_id");
                // appId
                Map<String, String> paymap = new HashMap<String, String>();
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
        return "wechat/pay";
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

}
