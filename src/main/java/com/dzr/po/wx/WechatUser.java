package com.dzr.po.wx;

import lombok.Data;

/**
 * @author dingzr
 * @Description
 * @ClassName WechatUser
 * @since 2017/7/28 14:49
 */

@Data
public class WechatUser {

    private Integer subscribe;
    private String openid;
    private String nickname;
    private Integer sex;
    private String language;
    private String city;
    private String province;
    private String country;
    private String headimgurl;
    private String unionid;
    private String remark;
    private Integer groupid;
}
