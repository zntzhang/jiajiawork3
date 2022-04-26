package com.example.jiajiawork3.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Date: 2022/4/5 12:23
 * @Description:
 */
public class ChajiUtils {

    public static String chaji(String value) {
        String str = "孟子皓\n" +
                "谢逸伊\n" +
                "赵聆茹\n" +
                "陈禹宏\n" +
                "张晋荣\n" +
                "杨宇辰\n" +
                "韩正扬\n" +
                "沈柳依\n" +
                "刘书锦\n" +
                "袁楚涵\n" +
                "陈路亚\n" +
                "余沛凝\n" +
                "赵思羽\n" +
                "顾嘉乐\n" +
                "戴可萱\n" +
                "陈周琰楠\n" +
                "戴语涵\n" +
                "徐筱淼\n" +
                "葛静怡\n" +
                "张涵雯\n" +
                "金杭\n" +
                "徐伟博\n" +
                "吴刘轩\n" +
                "林奕辰\n" +
                "汪恩泽\n" +
                "黄振哲\n" +
                "黄晨冉\n" +
                "陈枝圣\n" +
                "昝昱辰\n" +
                "刘思睿\n" +
                "许振宇\n" +
                "徐子桐";
        String[] split = str.split("\n");
        List<String> persons = new ArrayList<>(Arrays.asList(split));

        String lizi = value;

        String[] split2 = lizi.split("\n");
        List<String> persons2 = new ArrayList<>(Arrays.asList(split2));

        List<String> persons3 = new ArrayList<>();
        for (String person : persons) {
            boolean contain = false;
            for (String person2 : persons2) {
                if (person2.contains(person)) {
                    contain = true;
                    break;
                }
            }
            if (!contain) {
                persons3.add(person);
            }
        }
        return String.join(" , ", persons3);

    }

}
