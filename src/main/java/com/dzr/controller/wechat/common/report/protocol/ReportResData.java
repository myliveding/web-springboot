package com.dzr.controller.wechat.common.report.protocol;

import lombok.Data;

/**
 * Date: 2014/11/12
 * Time: 17:06
 */

@Data
public class ReportResData {

    //以下是API接口返回的对应数据
    private String return_code;
    private String return_msg;
    private String result_code;

}
