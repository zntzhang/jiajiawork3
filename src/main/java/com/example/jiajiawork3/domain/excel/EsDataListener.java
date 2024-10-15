package com.example.jiajiawork3.domain.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

/**
 * @author chunri
 * @ClassName DemoDataListener.java
 * @Description TODO
 * @createTime 2023年08月30日 17:26:00
 */
public class EsDataListener implements ReadListener<ExcelStudent> {

    private List<ExcelStudent> students = Lists.newArrayList();


    public List<ExcelStudent> getStudents() {
        return students;
    }

    @Override
    public void invoke(ExcelStudent student, AnalysisContext analysisContext) {
        students.add(student);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println(">>>>>> students" + students);
    }
}
