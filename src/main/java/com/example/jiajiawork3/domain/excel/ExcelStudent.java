package com.example.jiajiawork3.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author chunri
 * @ClassName excelStudent.java
 * @Description TODO
 * @createTime 2024年01月26日 10:32:00
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Document(indexName = "student")
public class ExcelStudent {
    @ExcelProperty("班级")
    @Field(type = FieldType.Keyword)
    private String className;

    @ExcelProperty("姓名")
    @Field(type = FieldType.Keyword)
    private String name;

    @ExcelProperty("分数")
    @Field(type = FieldType.Double)
    private Double score;

    @ExcelProperty("档位")
    @Field(type = FieldType.Keyword)
    private String level;

    public ExcelStudent(String className, String name, Double score, String level) {
        this.className = className;
        this.name = name;
        this.score = score;
        this.level = level;
    }


    @Field(type = FieldType.Keyword)
    private String type;

    @Id
    private Long id;
}
