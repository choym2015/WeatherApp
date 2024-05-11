package com.chopas.weatherapp.utils;

import junit.framework.TestCase;

import org.junit.Test;

public class TempUtilsTest extends TestCase {
    @Test
    public void testKelvinToFahrenheit() {
        final double sampleKelvin = 283.5;
        String convertedString = TempUtils.kelvinToFahrenheit(sampleKelvin);

        assertEquals(convertedString, "50\u2109");
    }
}