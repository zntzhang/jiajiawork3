package com.example.jiajiawork3.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @description:
 * @author: chunri
 * @create: 2022-08-05 13:53
 **/
@Getter
@Setter
public class RainbowPiResponse {

    private Integer code;
    private String msg;
    private List<RainbowPi> newslist;
}
