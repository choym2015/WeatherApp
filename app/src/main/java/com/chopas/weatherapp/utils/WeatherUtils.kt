package com.chopas.weatherapp.utils

import com.chopas.weatherapp.capitalizeWords
import com.chopas.weatherapp.model.WeatherData
import com.chopas.weatherapp.model.WeatherInfo

class WeatherUtils {
    companion object {
        /**
         * Parse WeatherInfo object to WeatherData object
         * @param weatherInfo object
         * @return WeatherData object
         */
        fun parseWeatherInfo(weatherInfo: WeatherInfo): WeatherData {
            return WeatherData(
                dateTime = DateUtils.unixTimestampToString(weatherInfo.dt),
                tempInFahrenheit = TempUtils.kelvinToFahrenheit(weatherInfo.main.temp),
                feelsLikeTempInFahrenheit = "Feels like: " + TempUtils.kelvinToFahrenheit(weatherInfo.main.feels_like),
                minTempInFahrenheit = "Min: " + TempUtils.kelvinToFahrenheit(weatherInfo.main.temp_min),
                maxTempInFahrenheit = "Max: " + TempUtils.kelvinToFahrenheit(weatherInfo.main.temp_max),
                weatherIcon = "https://openweathermap.org/img/wn/${weatherInfo.weather[0].icon}@2x.png",
                weatherDesc = weatherInfo.weather[0].description.capitalizeWords(),
                cityName = weatherInfo.name.capitalizeWords(),
                sunriseTime = DateUtils.unixTimestampToString(weatherInfo.sys.sunrise),
                sunsetTime = DateUtils.unixTimestampToString(weatherInfo.sys.sunset)
            )
        }
    }
}