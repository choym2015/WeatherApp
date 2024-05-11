package com.chopas.weatherapp.utils

import com.chopas.weatherapp.BuildConfig

class APIUtils {
    companion object {
        const val BASE_URL: String = "https://api.openweathermap.org/data/2.5/"
        const val API_KEY: String = BuildConfig.KEY // API_KEY must be declared in local.properties with key name "key"
    }
}