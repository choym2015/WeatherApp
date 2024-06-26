package com.chopas.weatherapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {
    /**
     * Convert UTC timestamp to hh:mm a format for readability
     * @param timestamp
     * @return String format of timestamp
     */
    public static String unixTimestampToString(Integer timestamp) {
        Date date = new java.util.Date((long) timestamp * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getDefault());

        return dateFormat.format(date);
    }
}
