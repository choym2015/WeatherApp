package com.chopas.weatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chopas.weatherapp.model.WeatherInfo
import com.chopas.weatherapp.service.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {
    val weatherInfoLiveData = MutableLiveData<WeatherInfo>()
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
            response.body()?.let { weatherInfo ->
                progressBarLiveData.postValue(false)
                weatherInfoLiveData.postValue(weatherInfo)
            } ?: run {
               //handle error
            }
        } else {
            //handle error
        }
    }
}