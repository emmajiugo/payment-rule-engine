package com.emmajiugo.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;

public class Utils {
    public static <T> T fromJson(String jsonString, Class<T> classOfT) {
        return new Gson().fromJson(jsonString, classOfT);
    }

    public static <T> List<T> fromJsonArray(String jsonString, Type listType) {
        return new Gson().fromJson(jsonString, listType);
    }

    public static String toJson(Object obj) {
        return new Gson().toJson(obj);
    }

}
