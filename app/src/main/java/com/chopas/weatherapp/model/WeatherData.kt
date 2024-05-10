package com.chopas.weatherapp.model

data class WeatherData(
    var dateTime: String = "",
    var tempInFahrenheit: String = "",
    var feelsLikeTempInFahrenheit: String = "",
    var minTempInFahrenheit: String = "",
    var maxTempInFahrenheit: String = "",
    var location: String = "",
    var weatherIcon: String = "",
    var weatherDesc: String = ""
)
