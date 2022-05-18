package com.example.jiajiawork3.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: jiajiawork3
 * @description: 缓存，单线程用map简单处理
 * @author: chunri
 * @create: 2022-05-18 19:42
 **/
public class CacheUtils {
    private static Map<String, String> map = new HashMap<>();

    public static String get(String key) {
        return map.get(key);
    }

    public static void set(String key, String value) {
        map.put(key, value);
    }
}
