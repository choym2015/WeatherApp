package com.chopas.weatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chopas.weatherapp.MainActivity
import com.chopas.weatherapp.model.WeatherData
import com.chopas.weatherapp.network.RetrofitInstance
import com.chopas.weatherapp.utils.WeatherUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {
    val weatherDataMutableLiveData = MutableLiveData<WeatherData>()
    val progressBarLiveData = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<Boolean>()

    /**
     * Use retrofit instance to fetch weatherInfo object using latitude and longitude.
     * Runs on background thread.
     * Post value to progressBarLiveData to show blurred progress view.
     *
     * Post weatherData object if successful.
     * Post error if unsuccessful.
     *
     * @param lat latitude in String
     * @param lon longitude in String
     */
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

                val weatherInfo = WeatherUtils.parseWeatherInfo(weatherInfo)
                weatherDataMutableLiveData.postValue(weatherInfo)
            } ?: run {
                errorLiveData.postValue(true)
            }
        } else {
            errorLiveData.postValue(true)
        }
    }

    /**
     * Use retrofit instance to fetch weatherInfo object using city name.
     * Runs on background thread.
     * Post value to progressBarLiveData to show blurred progress view.
     *
     * Post weatherData object if successful.
     * Post error if unsuccessful.
     *
     * Save city latitude and longitude if successful.
     *
     * @param lat latitude in String
     * @param lon longitude in String
     */
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

                val weatherInfo = WeatherUtils.parseWeatherInfo(weatherInfo)
                weatherDataMutableLiveData.postValue(weatherInfo)
            } ?: run {
                errorLiveData.postValue(true)
            }
        } else {
            errorLiveData.postValue(true)
        }
    }
}