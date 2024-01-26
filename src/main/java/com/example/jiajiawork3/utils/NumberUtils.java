package com.example.jiajiawork3.utils;

/**
 * @author chunri
 * @ClassName NumberUtils.java
 * @Description TODO
 * @createTime 2023年09月04日 16:37:00
 */
public class NumberUtils {

    /**
     * 保留两位小数，四舍五入的一个老土的方法
     *
     * @param d
     * @return
     */
    public static double formatDouble(double d) {
        return (double) Math.round(d * 100) / 100;
    }

}
