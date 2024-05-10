package com.chopas.weatherapp.network

import com.chopas.weatherapp.utils.APIUtils
import com.chopas.weatherapp.model.WeatherInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {
    @GET("weather?")
    fun getCurrentWeather(@Query("lat") lat: String, @Query("lon") lon: String, @Query("appid") appid: String = APIUtils.API_KEY) : Call<WeatherInfo>
    @GET("weather?")
    fun getCurrentWeatherByCity(@Query("q") city: String, @Query("appid") appid: String = APIUtils.API_KEY) : Call<WeatherInfo>
}