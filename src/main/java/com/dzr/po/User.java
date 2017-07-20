package com.dzr.po;

import lombok.Data;

/**
 * @author dingzr
 * @Description
 * @ClassName User
 * @since 2017/7/5 13:56
 */
@Data
public class User {

    private Integer id;
    private String userName;
    private String nickName;
    private String pwd;
    private Integer age;
    private String address;
    private String birthday;
    private String createTime;
    private String updateTime;
    private String isDelete;

    private City city;
}

