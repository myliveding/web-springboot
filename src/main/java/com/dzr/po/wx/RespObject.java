package com.dzr.po.wx;

/**
 * @author dingzr
 * @Description
 * @ClassName RespObject
 * @since 2017/6/7 15:25
 */
public class RespObject {

    private String respType; //应答类型

    private String respContent; //应答内容

    public String getRespType() {
        return respType;
    }

    public void setRespType(String respType) {
        this.respType = respType;
    }

    public String getRespContent() {
        return respContent;
    }

    public void setRespContent(String respContent) {
        this.respContent = respContent;
    }
}
