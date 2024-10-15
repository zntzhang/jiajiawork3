package com.example.jiajiawork3.utils.excel;

import cn.hutool.core.io.FileUtil;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.compress.utils.Sets;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class EventInfoExtractor {
    static List<String> event = Lists.newArrayList();
    public static void main(String[] args) {
        // 指定 txt 文件的路径
        String filePath = "/Users/admin/Desktop/code/";

        File rootFolder = new File(filePath);
// 获取目录中的所有文件
        File[] files = rootFolder.listFiles();
        Arrays.sort(files);
        System.out.println(Arrays.stream(files).map(File::getAbsolutePath).collect(Collectors.toList()));
        for (File file : files) {
            // 使用 Hutools 的 FileUtil 读取文件内容
            String sumJsonStr = FileUtil.readString(file.getAbsoluteFile(), "UTF-8");
            extractEventNames(sumJsonStr);
        }

        for (String s : event.stream().distinct().collect(Collectors.toList())) {
            System.out.println(s);
        }

    }

    private static void extractEventNames(String code) {
        // Define a regex pattern to match EventInfo("xxx")
        Pattern pattern = Pattern.compile("EventInfo\\(\"(.*?)\"\\)");

        // Create a matcher with the given code
        Matcher matcher = pattern.matcher(code);

        // Find and print all matches
        while (matcher.find()) {
            String eventName = matcher.group(1);
            event.add(eventName);
//            System.out.println(eventName);
        }
    }
}
