package com.example.jiajiawork3.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description: 自动回复
 * @author: chunri
 * @create: 2022-04-26 16:44
 **/
@Data
@TableName("auto_answer")
public class AutoAnswer {
    //主键
    @TableId(type = IdType.AUTO)
    private Long id;
    private String question;
    private String answer;
    private String questionId;
}
