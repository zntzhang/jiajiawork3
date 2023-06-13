package com.example.jiajiawork3.domain.chatgpt;

import lombok.Data;

/**
 * @author chunri
 * @ClassName Usage.java
 * @Description TODO
 * @createTime 2023年06月13日 15:07:00
 */
@Data
public class Usage {
    private long promptTokens;
    private long completionTokens;
    private long totalTokens;
}
