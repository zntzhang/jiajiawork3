package com.example.jiajiawork3.domain.chatgpt;

import lombok.Data;

/**
 * @author chunri
 * @ClassName ChatChoice.java
 * @Description TODO
 * @createTime 2023年06月13日 15:05:00
 */
@Data
public class ChatChoice {
    private long index;

    private Message message;

    private String finishReason;
}
