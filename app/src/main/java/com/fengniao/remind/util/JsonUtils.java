package com.fengniao.remind.util;


import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static <T> List<T> jsonToList(String data, Class<T> tClass) {
        List<T> mList = new ArrayList<>();
        if (TextUtils.isEmpty(data)) return mList;
        try {
            JSONArray mArray = new JSONArray(data);
            for (int i = 0; i < mArray.length(); i++) {
                T t = jsonToBean(mArray.get(i).toString(), tClass);
                mList.add(t);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mList;
    }

    public static <T> T jsonToBean(String data, Class<T> tClass) {
        return new Gson().fromJson(data, tClass);
    }

}
