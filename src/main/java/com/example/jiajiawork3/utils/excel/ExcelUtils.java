package com.example.jiajiawork3.utils.excel;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @program: EasyCalculate
 * @description:
 * @author: chunri
 * @create: 2022-01-23 09:48
 **/
public class ExcelUtils {
    public static void export(List<List<String>> rows, HttpServletResponse response) {
        //通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter();
        //跳过当前行，既第一行，非必须，在此演示用
        writer.passCurrentRow();

        //合并单元格后的标题行，使用默认标题样式
        // writer.merge(rows.get(0).size() - 1, "错题百分比");
        //一次性写出内容，强制输出标题
        writer.write(rows, true);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        long currentTime = System.currentTimeMillis();
        response.setHeader("Content-Disposition", "attachment;filename=" + "test" + currentTime + ".xls");
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            writer.flush(out, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭writer，释放内存
            writer.close();
            //此处记得关闭输出Servlet流
            IoUtil.close(out);
        }
        //关闭writer，释放内存
    }
}
