package com.xie.crm.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 格式化Date数据
 */
public class DateUtils {

    /**
     * 格式化Date数据
     * @param date
     * @return
     */
    public static String formatDateTime(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = dateFormat.format(date);
        return format;
    }
}
