package com.example.jiajiawork3.utils.excel;

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
 * @program: EasyCalculate
 * @description:
 * @author: chunri
 * @create: 2022-01-23 09:43
 **/
public class CalculateWorkV2 {

    public static void doExcel(InputStream inputStream, Integer num, HttpServletResponse response) {
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // TODO
        Map<Double, List<String>> scoreMap = new HashMap<>();
        List<List<Object>> readAll = reader.read();
        StringBuilder sumJsonStr = new StringBuilder();
        for (List<Object> objects : readAll) {
            String name = (String) objects.get(0);
            String jsonStr =(String)  objects.get(1);

            String[] array = jsonStr.split(" ");
            for (String s : array) {
                Double index = Double.valueOf(s);
                scoreMap.computeIfAbsent(index, k -> new ArrayList<>()).add(name);
            }
            sumJsonStr.append(jsonStr);
        }


        System.out.println(sumJsonStr.toString());
        String[] array = sumJsonStr.toString().split(" ");
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
        ExcelUtils.export(rows, response);
    }
}
