package com.dzr.controller.wechat;

import com.dzr.framework.config.Constant;
import com.dzr.framework.config.UrlConfig;
import com.dzr.po.wx.MessageUtil;
import com.dzr.util.StringUtils;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RequestMapping("/wechatPay")
@Controller
public class WechatNotifyController {

    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    UrlConfig urlConfig;

    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    public void execute(HttpServletRequest request, HttpServletResponse response) {
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
                logger.info("订单号orderNo:" + orderNo);
                logger.info("订单金额thirdPayAmt:" + thirdPayAmt);
                logger.info("支付回调用时获取的openid为：" + openid);

                /**
                 * 订单业务
                 */
                if (StringUtils.isNotEmpty(orderNo)) {
                    if (!"".equals(openid)) {
                        String[] arr = new String[]{"openid" + openid, "member_recharge_id" + orderNo, "serial_no" + serialNo};
                        String mystr = "openid=" + openid + "&member_recharge_id=" + orderNo + "&serial_no=" + serialNo;

                        JSONObject resultStr = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.RECHARGE_SUC, mystr, arr));
                        if (resultStr.containsKey("error_code") && 0 == resultStr.getInt("error_code")) {
                            logger.info("微信支付确认订单完成:" + orderNo);
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
