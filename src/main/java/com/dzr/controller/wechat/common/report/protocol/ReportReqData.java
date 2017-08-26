package com.dzr.controller.wechat.common.report.protocol;


import com.dzr.controller.wechat.common.Signature;
import com.dzr.framework.config.WechatParams;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 2014/11/12
 * Time: 17:05
 */
@XStreamAlias("xml")
public class ReportReqData {

    @Autowired
    WechatParams wechatParams;

    //每个字段具体的意思请查看API文档
    private String appid = wechatParams.getAppId();
    private String attach;
    private String body;
    private String mch_id = wechatParams.getMchId();
    private String nonce_str;
    private String notify_url = wechatParams.getNotifyUrl();
    private String openid;
    private String out_trade_no;
    private String spbill_create_ip;
    private String total_fee;
    private String trade_type = "JSAPI";
    private String sign;


    public ReportReqData(String goodname, String openid, String orderid, String ip, String totalfee, String nonceStr) {
        setAttach(goodname);
        setBody(goodname);
        setOpenid(openid);
        setNonce_str(nonceStr);
        setOut_trade_no(orderid);
        setTotal_fee(totalfee);
        String sign = Signature.getSign(toMap());
        setSign(sign);//把签名数据设置到Sign这个属性中
    }


    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object obj;
            try {
                obj = field.get(this);
                if (obj != null) {
                    map.put(field.getName(), obj);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }


    public String getAppid() {
        return appid;
    }


    public void setAppid(String appid) {
        this.appid = appid;
    }


    public String getAttach() {
        return attach;
    }


    public void setAttach(String attach) {
        this.attach = attach;
    }


    public String getBody() {
        return body;
    }


    public void setBody(String body) {
        this.body = body;
    }


    public String getMch_id() {
        return mch_id;
    }


    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }


    public String getNonce_str() {
        return nonce_str;
    }


    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getNotify_url() {
        return notify_url;
    }


    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }


    public String getOpenid() {
        return openid;
    }


    public void setOpenid(String openid) {
        this.openid = openid;
    }


    public String getOut_trade_no() {
        return out_trade_no;
    }


    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }


    public String getSpbill_create_ip() {
        return spbill_create_ip;
    }


    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }


    public String getTotal_fee() {
        return total_fee;
    }


    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }


    public String getTrade_type() {
        return trade_type;
    }


    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }


    public String getSign() {
        return sign;
    }


    public void setSign(String sign) {
        this.sign = sign;
    }


}
