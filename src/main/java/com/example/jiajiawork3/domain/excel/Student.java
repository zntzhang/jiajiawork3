package com.example.jiajiawork3.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author chunri
 * @ClassName Student.java
 * @Description TODO
 * @createTime 2023年08月30日 11:11:00
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
public class Student {
    @ExcelProperty("班级")
    private String className;
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("分数")
    private Double score;
    @ExcelProperty("全国学籍号")
    private String countryNo;
    @ExcelProperty("学籍辅号")
    private String no;
    private String level;
    private Long id;

}
