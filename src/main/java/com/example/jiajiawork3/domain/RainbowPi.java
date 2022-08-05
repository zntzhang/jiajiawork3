package com.example.jiajiawork3.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @program: jiajiawork3
 * @description:
 * @author: chunri
 * @create: 2022-08-05 13:54
 **/
@Getter
@Setter
@TableName("rainbow_pi")
public class RainbowPi {
    //主键
    @TableId(type = IdType.AUTO)
    private Long id;
    private String content;
    private Date created;
}
