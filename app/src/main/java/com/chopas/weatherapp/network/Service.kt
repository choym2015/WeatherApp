package com.chopas.weatherapp.network

import com.chopas.weatherapp.utils.APIUtils
import com.chopas.weatherapp.model.WeatherInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {
    /**
     * API Call to get weather with latitude and longitude
     *
     * @param lat latitude
     * @param lon longitude
     * @param appid API_KEY declared in local.properties
     *
     * @return WeatherInfo object enclosed in Call
     */
    @GET("weather?")
    fun getCurrentWeather(@Query("lat") lat: String, @Query("lon") lon: String, @Query("appid") appid: String = APIUtils.API_KEY) : Call<WeatherInfo>

    /**
     * API Call to get weather with city name
     *
     * @param city city name
     * @param appid API_KEY declared in local.properties
     *
     * @return WeatherInfo object enclosed in Call
     */
    @GET("weather?")
    fun getCurrentWeatherByCity(@Query("q") city: String, @Query("appid") appid: String = APIUtils.API_KEY) : Call<WeatherInfo>
}