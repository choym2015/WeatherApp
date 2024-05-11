package com.chopas.weatherapp.utils

import com.chopas.weatherapp.BuildConfig
import java.io.File
import java.io.FileInputStream
import java.util.Properties

class APIUtils {
    companion object {
        const val BASE_URL: String = "https://api.openweathermap.org/data/2.5/"
        val API_KEY: String = BuildConfig.KEY
    }
}