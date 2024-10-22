package com.happychat.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class CopyTools {
    public static <T, S> T copy(S s, Class<T> clazz) {
        T t = null;
        try {
            t = clazz.newInstance();

        } catch (Exception e) {
            e.printStackTrace();
        }
        BeanUtils.copyProperties(s, t);
        return t;
    }

    public static <T, S> List<T> copyList(List<S> sList, Class<T> clazz) {
        List<T> tList = new ArrayList<>();
        for (S s : sList) {
            T t = null;
            try {
                t = clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            BeanUtils.copyProperties(s, t);
            tList.add(t);
        }
        return tList;
    }
}
