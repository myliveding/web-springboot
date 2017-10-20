package com.dzr.po;

import lombok.Data;

/**
 * @author dingzr
 * @Description 微信页面分享所用各个属性
 * @ClassName WechatShare
 * @since 2017/10/20 10:19
 */

@Data
public class WechatShare {

    /**
     * 加密之后字段
     */
    private String signature;

    /**
     * js调用凭据
     */
    private String jsapiTicket;

    /**
     * 时间戳
     */
    private String timestamp;

    /**
     * 随机数
     */
    private String noncestr;

    /**
     * 公众号ID
     */
    private String appId;

    /**
     * 分享得url
     */
    private String shareUrl;

}
