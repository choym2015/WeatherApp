package com.chopas.weatherapp.utils;

import junit.framework.TestCase;

import org.junit.Test;


public class DateUtilsTest extends TestCase {
    @Test
    public void testUnixTimestampToString() {
        final int sampleUnixTimestamp = 1715353547;
        String convertedTime = DateUtils.unixTimestampToString(sampleUnixTimestamp);

        assertEquals(convertedTime, "12:05 AM");
    }
}