package com.chopas.weatherapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String unixTimestampToString(Integer timestamp) {
        Date date = new java.util.Date((long) timestamp * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm");

        return dateFormat.format(date);
    }
}
