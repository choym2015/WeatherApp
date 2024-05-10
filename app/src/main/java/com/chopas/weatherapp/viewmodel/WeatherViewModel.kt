package com.chopas.weatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chopas.weatherapp.capitalizeWords
import com.chopas.weatherapp.utils.DateUtils
import com.chopas.weatherapp.utils.TempUtils
import com.chopas.weatherapp.model.WeatherData
import com.chopas.weatherapp.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {
    val weatherDataMutableLiveData = MutableLiveData<WeatherData>()
    val progressBarLiveData = MutableLiveData<Boolean>()

    fun getWeather(city: String? = null) = viewModelScope.launch(Dispatchers.IO) {
        progressBarLiveData.postValue(true)

        val apiCall = if (city == null) { // if there is no city defined
            RetrofitInstance.api.getCurrentWeather("37.55", "127")
        } else { // user has defined a city
            RetrofitInstance.api.getCurrentWeather("37.55", "127")
        }

        val response = apiCall.execute()

        if (response.isSuccessful) {
            progressBarLiveData.postValue(false)

            response.body()?.let { weatherInfo ->
                if (weatherInfo.weather.isEmpty()) {
                    //handle error here
                    return@let
                }

                val weatherData = WeatherData(
                    dateTime = DateUtils.unixTimestampToString(weatherInfo.dt),
                    tempInFahrenheit = TempUtils.kelvinToFahrenheit(weatherInfo.main.temp),
                    feelsLikeTempInFahrenheit = "Feels like: " + TempUtils.kelvinToFahrenheit(weatherInfo.main.feels_like),
                    minTempInFahrenheit = "Min: " + TempUtils.kelvinToFahrenheit(weatherInfo.main.temp_min),
                    maxTempInFahrenheit = "Max: " + TempUtils.kelvinToFahrenheit(weatherInfo.main.temp_max),
                    weatherIcon = "https://openweathermap.org/img/wn/${weatherInfo.weather[0].icon}@2x.png",
                    weatherDesc = weatherInfo.weather[0].description.capitalizeWords()
                )

                weatherDataMutableLiveData.postValue(weatherData)
            } ?: run {
               //handle error
            }
        } else {
            progressBarLiveData.postValue(false)
            //show error
        }
    }
}