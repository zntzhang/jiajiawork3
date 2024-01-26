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
public class DemoDataListener implements ReadListener<Student> {

    private List<Student> students = Lists.newArrayList();

    private Long number = 1L;

    public List<Student> getStudents() {
        return students;
    }

    @Override
    public void invoke(Student student, AnalysisContext analysisContext) {
        student.setId(number++);
        students.add(student);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println(">>>>>> students" + students);
    }
}
