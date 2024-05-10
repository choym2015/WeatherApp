package com.chopas.weatherapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.chopas.weatherapp.viewmodel.WeatherViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var weatherViewModel: WeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        weatherViewModel.getWeather()
    }
}
