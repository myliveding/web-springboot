package com.dzr.po.resp;

import lombok.Data;

@Data
public class ImageMessage extends BaseMessage {

    // 图片链接
    private Image image;

}