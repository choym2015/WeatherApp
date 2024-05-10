package com.chopas.weatherapp.utils;

public class TempUtils {
    public static String kelvinToFahrenheit(Double tempInKelvin) {
        return (int) ((tempInKelvin * 9/5) - 459.67) + "\u2109"; // append Fahrenheit symbol
    }
}
