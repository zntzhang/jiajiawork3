package com.example.jiajiawork3.utils.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.example.jiajiawork3.domain.excel.Class;
import com.example.jiajiawork3.domain.excel.DemoDataListener;
import com.example.jiajiawork3.domain.excel.ExcelStudent;
import com.example.jiajiawork3.domain.excel.Student;
import com.example.jiajiawork3.utils.NumberUtils;
import org.apache.commons.compress.utils.Lists;
import org.junit.Test;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author chunri
 * @ClassName ScoreExcel.java
 * @Description TODO
 * @createTime 2023年08月30日 10:49:00
 */
public class ScoreExcel {
    @Test
    public void repeatedRead() {
        String fileName = "/Users/admin/Downloads/【第一单元成绩】501-504、507.xlsx";
        int lastSeparatorIndex = fileName.lastIndexOf('/');
        int extensionIndex = fileName.lastIndexOf('.');
        String extractedName = fileName.substring(lastSeparatorIndex + 1, extensionIndex);
        // 读取全部sheet
        // 这里需要注意 DemoDataListener的doAfterAllAnalysed 会在每个sheet读取完毕后调用一次。然后所有sheet都会往同一个DemoDataListener里面写
        DemoDataListener demoDataListener = new DemoDataListener();
        EasyExcel.read(fileName, Student.class, demoDataListener).doReadAll();
        List<Student> students = demoDataListener.getStudents()
                .stream().filter(s -> !StringUtils.isEmpty(s.getClassName()))
                .collect(Collectors.toList());
        TreeMap<String, List<Student>> classStudentMap = students.stream().collect(Collectors.groupingBy(Student::getClassName,TreeMap::new,Collectors.toList()));

        students = students.stream().filter(s -> !StringUtils.isEmpty(s.getScore()))
                .sorted(Comparator.comparing(Student::getScore).reversed()).collect(Collectors.toList());
        int studentSize = students.size();
        List<Class> classList = Lists.newArrayList();
        String templateFileName = "src/main/resources/excel/template"+ ".xls";
        String toFileName = "/Users/admin/Documents/student/"+extractedName+"统计表.xls" + System.currentTimeMillis() + ".xls";

        double cutoffScore = students.stream()
                .skip((int) Math.round(studentSize * 0.2) - 1)
                .findFirst()
                .map(Student::getScore)
                .orElse(0.0);
        // A档算法 ：20% 同分都算,更接近20%
        List<Student> top20PercentStudents_1 = students.stream()
                .filter(student -> student.getScore() >= cutoffScore)
                .collect(Collectors.toList());

        List<Student> top20PercentStudents_2 = students.stream()
                .filter(student -> student.getScore() > cutoffScore)
                .collect(Collectors.toList());
        int diff20_1 = Math.abs(top20PercentStudents_1.size() - (int)(studentSize * 0.2));
        int diff20_2 = Math.abs(top20PercentStudents_2.size() - (int)(studentSize * 0.2));
        List<Student> top20PercentStudents = diff20_1 < diff20_2 ? top20PercentStudents_1 : top20PercentStudents_2;
        Map<String, List<Student>> classStudentAMap = top20PercentStudents.stream().collect(Collectors.groupingBy(Student::getClassName, Collectors.toList()));

        int cutoffIndexB = top20PercentStudents.size() + (int) Math.round(studentSize * 0.25);
        System.out.println("cutoffIndexB" +cutoffIndexB);
        double cutoffScoreB = students.get(cutoffIndexB - 1).getScore();

        List<Student> top25PercentStudentsB_1 = students.stream()
                .filter(student -> student.getScore() >= cutoffScoreB)
                .collect(Collectors.toList());
        List<Student> top25PercentStudentsB_2 = students.stream()
                .filter(student -> student.getScore() > cutoffScoreB)
                .collect(Collectors.toList());
        // B档算法 ：同分都算和同分都不算，A+B哪个更接近45%
        int diff1 = Math.abs(top25PercentStudentsB_1.size() - (int)(studentSize * 0.45));
        int diff2 = Math.abs(top25PercentStudentsB_2.size() - (int)(studentSize * 0.45));
        List<Student> top25PercentStudentsB = diff1 < diff2 ? top25PercentStudentsB_1 : top25PercentStudentsB_2;
        System.out.println("top25PercentStudentsB" +top25PercentStudentsB.size());
        top25PercentStudentsB.removeAll(top20PercentStudents);
        Map<String, List<Student>> classStudentBMap = top25PercentStudentsB.stream().collect(Collectors.groupingBy(Student::getClassName, Collectors.toList()));

        System.out.println("top20PercentStudents" +top20PercentStudents.size());
        System.out.println("-top25PercentStudentsB" +top25PercentStudentsB.size());
        int cutoffIndexC = top20PercentStudents.size() + top25PercentStudentsB.size() + (int) Math.round(studentSize * 0.25);
        System.out.println("cutoffIndexC" +cutoffIndexC);
        double cutoffScoreC = students.get(cutoffIndexC - 1).getScore();
        List<Student> top25PercentStudentsC_1 = students.stream()
                .filter(student -> student.getScore() >= cutoffScoreC)
                .collect(Collectors.toList());
        List<Student> top25PercentStudentsC_2 = students.stream()
                .filter(student -> student.getScore() > cutoffScoreC)
                .collect(Collectors.toList());
        int diffc1 = Math.abs(top25PercentStudentsC_1.size() - (int)(studentSize * 0.7));
        int diffc2 = Math.abs(top25PercentStudentsC_2.size() - (int)(studentSize * 0.7));
        List<Student> top25PercentStudentsC = diffc1 < diffc2 ? top25PercentStudentsC_1 : top25PercentStudentsC_2;

        top25PercentStudentsC.removeAll(top25PercentStudentsB);
        top25PercentStudentsC.removeAll(top20PercentStudents);
        Map<String, List<Student>> classStudentCMap = top25PercentStudentsC.stream().collect(Collectors.groupingBy(Student::getClassName, Collectors.toList()));

        int cutoffIndexD = top20PercentStudents.size() + top25PercentStudentsB.size() + top25PercentStudentsC.size() + (int) Math.round(studentSize * 0.2);
        double cutoffScoreD = students.get(cutoffIndexD - 1).getScore();
        List<Student> top20PercentStudentsD_1 = students.stream()
                .filter(student -> student.getScore() >= cutoffScoreD)
                .collect(Collectors.toList());
        List<Student> top20PercentStudentsD_2 = students.stream()
                .filter(student -> student.getScore() > cutoffScoreD)
                .collect(Collectors.toList());
        int diffd1 = Math.abs(top20PercentStudentsD_1.size() - (int)(studentSize * 0.9));
        int diffd2 = Math.abs(top20PercentStudentsD_2.size() - (int)(studentSize * 0.9));
        List<Student> top20PercentStudentsD = diffd1 < diffd2 ? top20PercentStudentsD_1 : top20PercentStudentsD_2;

        top20PercentStudentsD.removeAll(top25PercentStudentsC);
        top20PercentStudentsD.removeAll(top25PercentStudentsB);
        top20PercentStudentsD.removeAll(top20PercentStudents);
        Map<String, List<Student>> classStudentDMap = top20PercentStudentsD.stream().collect(Collectors.groupingBy(Student::getClassName, Collectors.toList()));

        List<Student> top10PercentStudentsE = new ArrayList<>(students);
        top10PercentStudentsE.removeAll(top20PercentStudentsD);
        top10PercentStudentsE.removeAll(top25PercentStudentsC);
        top10PercentStudentsE.removeAll(top25PercentStudentsB);
        top10PercentStudentsE.removeAll(top20PercentStudents);
        Map<String, List<Student>> classStudentEMap = top10PercentStudentsE.stream().collect(Collectors.groupingBy(Student::getClassName, Collectors.toList()));


        for (Map.Entry<String, List<Student>> entry : classStudentMap.entrySet()) {
            List<Student> studentList = entry.getValue();
            String className = entry.getKey();
            List<Student> acStudents = studentList.stream().filter(s -> !StringUtils.isEmpty(s.getScore())).collect(Collectors.toList());
            double averageScore = NumberUtils.formatDouble(acStudents.stream().collect(Collectors.summarizingDouble(Student::getScore)).getAverage());

            // 使用流来排序学生并计算后30%的平均分
            double lastThreeAverage = acStudents.stream()
                    .sorted(Comparator.comparing(Student::getScore).reversed()) // 按成绩降序排序
                    .skip((int) (Math.round(acStudents.size() * 0.7))) // 跳过前70%的学生
                    .mapToDouble(Student::getScore) // 获取成绩
                    .average() // 计算平均值
                    .orElse(0.0); // 如果列表为空，返回0.0

            // 使用流筛选出60分以下的学生，并构建格式化字符串
            String sixStudent = acStudents.stream()
                    .filter(student -> student.getScore() < 60.0)
                    .map(student -> {
                        double score = student.getScore();
                        if (score == (int) score) {
                            return student.getName() + " " + (int) score;
                        } else {
                            return student.getName() + " " + score;
                        }
                    })
                    .collect(Collectors.joining("、"));

            List<Student> aStudents = classStudentAMap.getOrDefault(className, Lists.newArrayList());
            List<Student> bStudents = classStudentBMap.getOrDefault(className, Lists.newArrayList());
            List<Student> cStudents = classStudentCMap.getOrDefault(className, Lists.newArrayList());
            List<Student> dStudents = classStudentDMap.getOrDefault(className, Lists.newArrayList());
            List<Student> eStudents = classStudentEMap.getOrDefault(className, Lists.newArrayList());

            aStudents.forEach(s ->s.setLevel("A"));
            bStudents.forEach(s ->s.setLevel("B"));
            cStudents.forEach(s ->s.setLevel("C"));
            dStudents.forEach(s ->s.setLevel("D"));
            eStudents.forEach(s ->s.setLevel("E"));

            Class fillData = new Class();
            fillData.setClassName(className);
            fillData.setNum(studentList.size());
            fillData.setAcNum(acStudents.size());
            fillData.setAvScore(averageScore);
            fillData.setLastThreeScore(lastThreeAverage);
            fillData.setAlevelNum(aStudents.size());
            fillData.setAlevelPercent(getPercent(acStudents, aStudents));
            fillData.setBlevelNum(bStudents.size());
            fillData.setBlevelPercent(getPercent(acStudents, bStudents));
            List<Student> abList = new ArrayList<>(aStudents);
            abList.addAll(bStudents);
            fillData.setAblevelPercent(getPercent(acStudents, abList));
            fillData.setClevelNum(cStudents.size());
            fillData.setClevelPercent(getPercent(acStudents, cStudents));
            fillData.setDlevelNum(dStudents.size());
            fillData.setDlevelPercent(getPercent(acStudents, dStudents));
            fillData.setElevelNum(eStudents.size());
            fillData.setElevelPercent(getPercent(acStudents, eStudents));
            fillData.setSixStatus(sixStudent);
            fillData.setTeacher("宝宝");
            classList.add(fillData);


        }
//        EasyExcel.write(toFileName).withTemplate(templateFileName).sheet().doFill(classList);
        // 方案1
        try (ExcelWriter excelWriter = EasyExcel.write(toFileName).withTemplate(templateFileName).build()) {
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            // 这里注意 入参用了forceNewRow 代表在写入list的时候不管list下面有没有空行 都会创建一行，然后下面的数据往后移动。默认 是false，会直接使用下一行，如果没有则创建。
            // forceNewRow 如果设置了true,有个缺点 就是他会把所有的数据都放到内存了，所以慎用
            // 简单的说 如果你的模板有list,且list不是最后一行，下面还有数据需要填充 就必须设置 forceNewRow=true 但是这个就会把所有数据放到内存 会很耗内存
            // 如果数据量大 list不是最后一行 参照下一个
            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
            excelWriter.fill(classList, fillConfig, writeSheet);
            Map<String, Object> map = MapUtils.newHashMap();
            map.put("level", String.format("A %s B %s C %s D %s E %s"
                    , top20PercentStudents.get(top20PercentStudents.size() -1).getScore()
                    , top25PercentStudentsB.get(top25PercentStudentsB.size() -1).getScore()
                    , top25PercentStudentsC.get(top25PercentStudentsC.size() -1).getScore()
                    , top20PercentStudentsD.get(top20PercentStudentsD.size() -1).getScore()
                    , top10PercentStudentsE.get(top10PercentStudentsE.size() -1).getScore()
            ));
            map.put("excelName",extractedName);
            excelWriter.fill(map, writeSheet);
        }

        List<ExcelStudent> excelStudents = Lists.newArrayList();
        for (Student student : students) {
            excelStudents.add(new ExcelStudent(student.getClassName(), student.getName(), student.getScore(), student.getLevel()));
        }
        List<ExcelStudent> toStudents = excelStudents.stream().sorted(Comparator.comparing(ExcelStudent::getClassName)).collect(Collectors.toList());
        String studentFileName = "/Users/admin/Documents/student/" +extractedName +"成绩等级" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(studentFileName, ExcelStudent.class)
                .sheet("")
                .doWrite(() -> {
                    // 分页查询数据
                    return toStudents;
                });


    }

    private static String getPercent(List<Student> acStudents, List<Student> twentyStudents) {
        double percentage = ((double) twentyStudents.size() / acStudents.size()) * 100.0;
        String formattedPercentage = String.format("%.2f%%", percentage); // 格式化为带百分号的字符串
        return formattedPercentage;
    }

}
