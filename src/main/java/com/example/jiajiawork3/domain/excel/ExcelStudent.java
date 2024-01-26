package com.example.jiajiawork3.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author chunri
 * @ClassName excelStudent.java
 * @Description TODO
 * @createTime 2024年01月26日 10:32:00
 */
@Getter
@Setter
@AllArgsConstructor
public class ExcelStudent {
    @ExcelProperty("班级")
    private String className;
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("分数")
    private Double score;
    @ExcelProperty("档位")
    private String level;
}
