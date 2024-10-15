package com.example.jiajiawork3.utils.excel;

import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import org.apache.commons.compress.utils.Lists;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chunri
 * @ClassName CalculateWork.java
 * @Description TODO
 * @createTime 2024年01月31日 11:10:00
 */
public class CalculateWork {
    static Integer num =34 ;
    public static void doExcel() {
        // 指定 txt 文件的路径
        String filePath = "/Users/admin/Downloads/cuoti0701/510错题统计(1).txt";

        // 使用 Hutools 的 FileUtil 读取文件内容
        String sumJsonStr = FileUtil.readString(filePath, "UTF-8");
        System.out.println(sumJsonStr);
        String[] array = sumJsonStr.split(" ");
        List<Double> indexs = new ArrayList<Double>();
        for (String s : array) {
            Double index = Double.valueOf(s);
            indexs.add(index);
        }
        Map<Double, Integer> sumMap = new HashMap<Double, Integer>();
        for (Double index : indexs) {
            if (sumMap.containsKey(index)) {
                Integer integer = sumMap.get(index);
                integer += 1;
                sumMap.put(index, integer);
            } else {
                sumMap.put(index, 1);
            }
        }

        List<List<String>> rows = Lists.newArrayList();
        for (Map.Entry<Double, Integer> index2PercentMap : sumMap.entrySet()) {
            List<String> arrayList = Lists.newArrayList();
            Integer count = index2PercentMap.getValue();
//            if (count < 4) {
//                continue;
//            }
            NumberFormat numberFormat = NumberFormat.getInstance();
            // 设置精确到小数点后2位
            numberFormat.setMaximumFractionDigits(2);
            String result = numberFormat.format((1 - (float) count / (float) num) * 100);
            Double index = index2PercentMap.getKey();
            System.out.println(index + " : " + result + "%");
            arrayList.add(index.toString());
            arrayList.add(result + "%");

            rows.add(arrayList);
        }
    }

    public static void main(String[] args) {
        doExcel();
    }
}
