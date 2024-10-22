package com.happychat.utils;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.text.ParseException;

public class DateUtils {
    private static final Object lockObj = new Object();
    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<>();

    private static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);
        if (tl == null) {
            synchronized (lockObj) {
                tl = sdfMap.get(pattern);
                if (tl == null) {
                    tl = ThreadLocal.withInitial(() -> new SimpleDateFormat(pattern));
                    sdfMap.put(pattern, tl);
                }
            }
        }
        return tl.get();
    }

    public static String format(Date date, String pattern) {
        SimpleDateFormat sdf = getSdf(pattern);
        System.out.println("Formatting date: " + date + " with pattern: " + pattern);
        return sdf.format(date);
    }

    public static Date parse(String dateStr, String pattern) {
        try {
            System.out.println("Parsing date string: " + dateStr + " with pattern: " + pattern);
            return getSdf(pattern).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }
}
