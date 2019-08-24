package com.bittech.java.util;

/**
 * 封装公共工具方法，如加载配置文件，json序列化等
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CommUtils {
    private static final Gson GSON = new GsonBuilder().create(); //创建gson对象

    /**
     * 加载配置文件
     *
     * @param fileName 要加载的配置文件名称
     * @return
     */
    //ctrl + shift + T
    public static Properties loadProperties(String fileName) {   //返回一个配置文件
        Properties properties = new Properties();
        InputStream in = CommUtils.class.getClassLoader().getResourceAsStream(fileName);
        try {
            properties.load(in); //输入流加载
        } catch (IOException e) {
            return null;
        }
        return properties;
    }

    /**
     * 将任意对象序列化为json字符串
     *
     * @param obj
     * @return
     */
    public static String object2Json(Object obj) {
        return GSON.toJson(obj);
    }
    /**
     * 将任意对象序列化为json字符串, 用到反射，类的反射对象
     * @param josnStr json字符串
     * @param objClass 要反序列化的类反射对象
     * @return
     */
    public static Object json2Object(String josnStr, Class objClass) {
        return GSON.fromJson(josnStr, objClass);
    }
}
