package com.chopas.weatherapp.model

/**
 * Custom WeatherData class that contains useful information parsed from the WeatherInfo object
 */
data class WeatherData(
    var dateTime: String = "",
    var tempInFahrenheit: String = "",
    var feelsLikeTempInFahrenheit: String = "",
    var minTempInFahrenheit: String = "",
    var maxTempInFahrenheit: String = "",
    var location: String = "",
    var weatherIcon: String = "",
    var weatherDesc: String = "",
    var cityName: String = "",
    var sunriseTime: String = "",
    var sunsetTime: String = ""
)
