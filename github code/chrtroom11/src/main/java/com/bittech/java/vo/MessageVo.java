package com.bittech.java.vo;

import lombok.Data;

/**
 * 服务器与客户端传递信息的载体
 */
@Data
public class MessageVo {
    /**
     * 表示告知服务器要进行的动作，1表示用户注册，2表示私聊
     */
    private String type;
    /**
     * 发送到服务器的具体内容
     */
    private String content;
    private String to;


}
