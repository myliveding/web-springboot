package com.dzr.po.wx;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author dingzr
 * @Description
 * @ClassName WechatUser
 * @since 2017/7/28 14:49
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WechatUser {

    private String subscribe;
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

    private Integer errcode; //错误code
    private String errmsg; //错误信息
}
