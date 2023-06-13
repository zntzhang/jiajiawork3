package com.example.jiajiawork3.domain.chatgpt;

import lombok.Data;

/**
 * @author chunri
 * @ClassName Message.java
 * @Description TODO
 * @createTime 2023年06月13日 15:06:00
 */
@Data
public class Message {
    private String role;
    private String content;
}
