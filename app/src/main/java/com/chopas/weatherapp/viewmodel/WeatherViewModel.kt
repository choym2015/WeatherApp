package com.chopas.weatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chopas.weatherapp.MainActivity
import com.chopas.weatherapp.capitalizeWords
import com.chopas.weatherapp.utils.DateUtils
import com.chopas.weatherapp.utils.TempUtils
import com.chopas.weatherapp.model.WeatherData
import com.chopas.weatherapp.model.WeatherInfo
import com.chopas.weatherapp.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {
    val weatherDataMutableLiveData = MutableLiveData<WeatherData>()
    val progressBarLiveData = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<Boolean>()

    fun getWeather(lat: String, lon: String) = viewModelScope.launch(Dispatchers.IO) {
        progressBarLiveData.postValue(true)

        val response = RetrofitInstance.api.getCurrentWeather(lat, lon).execute()

        if (response.isSuccessful) {
            progressBarLiveData.postValue(false)

            response.body()?.let { weatherInfo ->
                if (weatherInfo.weather.isEmpty()) {
                    errorLiveData.postValue(true)
                    return@let
                }

                parseWeatherInfo(weatherInfo)
            } ?: run {
                errorLiveData.postValue(true)
            }
        } else {
            errorLiveData.postValue(true)
        }
    }

    fun getWeatherByCity(city: String) {
        progressBarLiveData.postValue(true)

        val response = RetrofitInstance.api.getCurrentWeatherByCity(city).execute()
        if (response.isSuccessful) {
            progressBarLiveData.postValue(false)

            response.body()?.let { weatherInfo ->
                if (weatherInfo.weather.isEmpty()) {
                    errorLiveData.postValue(true)
                    return@let
                }

                with (MainActivity.sharedPref.edit()) {
                    putString("lat", weatherInfo.coord.lat.toString())
                    putString("lon", weatherInfo.coord.lon.toString())
                    apply()
                }
                parseWeatherInfo(weatherInfo)
            } ?: run {
                errorLiveData.postValue(true)
            }
        } else {
            errorLiveData.postValue(true)
        }
    }

    private fun parseWeatherInfo(weatherInfo: WeatherInfo) {
        val weatherData = WeatherData(
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

        weatherDataMutableLiveData.postValue(weatherData)
    }
}