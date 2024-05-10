package com.chopas.weatherapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.chopas.weatherapp.databinding.ActivityMainBinding
import com.chopas.weatherapp.model.WeatherData
import com.chopas.weatherapp.viewmodel.WeatherViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var weatherViewModel: WeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        setViewModelListeners()
        setSearchViewListener()

        weatherViewModel.getWeather()
    }

    private fun setViewModelListeners() {
        weatherViewModel.weatherDataMutableLiveData.observe(this) { weatherData ->
            updateViews(weatherData)
        }
    }

    private fun setSearchViewListener() {
        binding.citySearchView.setOnQueryTextListener(object: OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                query?.let { city ->

                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun updateViews(weatherData: WeatherData?) {
        weatherData?.let {
            binding.dateTimeTextView.text = it.dateTime
            binding.weatherFahrenheitTextView.text = it.tempInFahrenheit
            binding.weatherMinTextView.text = it.minTempInFahrenheit
            binding.weatherMaxTextView.text = it.maxTempInFahrenheit
            binding.weatherFeelsTextView.text = it.feelsLikeTempInFahrenheit
            binding.weatherDescTextView.text = it.weatherDesc

            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)

            Glide.with(this).load(it.weatherIcon).apply(requestOptions).into(binding.weatherIconImageView)
        } ?: run {
            //show error message
        }
    }
}
