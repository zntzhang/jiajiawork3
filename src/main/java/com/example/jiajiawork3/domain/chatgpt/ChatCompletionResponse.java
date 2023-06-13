package com.example.jiajiawork3.domain.chatgpt;

import lombok.Data;

import java.util.List;

/**
 * @author chunri
 * @ClassName ChatCompletionResponse.java
 * @Description TODO
 * @createTime 2023年06月13日 15:05:00
 */
@Data
public class ChatCompletionResponse {
    private String id;
    private String object;
    private long created;
    private String model;
    private List<ChatChoice> choices;
    private Usage usage;
}
