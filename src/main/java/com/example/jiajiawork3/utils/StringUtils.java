package com.example.jiajiawork3.utils;

/**
 * @description:
 * @author: chunri
 * @create: 2022-04-26 17:32
 **/
public class StringUtils {
    public static String subString(String str, String strStart, String strEnd) {

        /* 找出指定的2个字符在 该字符串里面的 位置 */
        int strStartIndex = str.indexOf(strStart);
        int strEndIndex = str.indexOf(strEnd);

        /* index 为负数 即表示该字符串中 没有该字符 */
        if (strStartIndex < 0 || strEndIndex < 0) {
            return "";
        }
        return str.substring(strStartIndex, strEndIndex).substring(strStart.length());
    }

    public static String subStringEnd(String str, String strStart) {

        /* 找出指定的2个字符在 该字符串里面的 位置 */
        int strStartIndex = str.indexOf(strStart);
        int strEndIndex = str.length();

        /* index 为负数 即表示该字符串中 没有该字符 */
        if (strStartIndex < 0) {
            return "";
        }
        return str.substring(strStartIndex, strEndIndex).substring(strStart.length());
    }

    public static void main(String[] args) {
        String str = "123456";
        String s1 = subString(str, "2", "6");
        String s2 = subStringEnd(str, "2");
        System.out.println(s1);
        System.out.println(s2);
    }
}
