package com.chopas.weatherapp

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.chopas.weatherapp.databinding.ActivityMainBinding
import com.chopas.weatherapp.model.WeatherData
import com.chopas.weatherapp.viewmodel.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        private const val LOCATION_PERMISSION_CODE = 101
        lateinit var sharedPref: SharedPreferences
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        sharedPref = this.getSharedPreferences(getString(R.string.shared_pref_name), Context.MODE_PRIVATE)

        setViewModelListeners()
        setSearchViewListener()

        val lastLat = sharedPref.getString(getString(R.string.latitude_shared_pref_key), null)
        val lastLon = sharedPref.getString(getString(R.string.longitude_shared_pref_key), null)

        if (lastLat != null && lastLon != null) {
            weatherViewModel.getWeather(lastLat, lastLon)
        } else {
            fetchLocation()
        }
    }

    private fun fetchLocation() {
        val task = fusedLocationProviderClient.lastLocation

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_CODE)
            return
        }

        task.addOnSuccessListener {
            if (it != null) {
                val latitude = it.latitude.toString()
                val longitude = it.longitude.toString()

                with (sharedPref.edit()) {
                    putString(getString(R.string.latitude_shared_pref_key), latitude)
                    putString(getString(R.string.longitude_shared_pref_key), longitude)
                    apply()
                }
                weatherViewModel.getWeather(latitude, longitude)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            } else {
//                Toast.makeText(this@MainActivity, "Camera Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
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
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)

            Glide.with(this).load(it.weatherIcon).apply(requestOptions).into(binding.weatherIconImageView)

            binding.dateTimeTextView.text = it.dateTime
            binding.weatherFahrenheitTextView.text = it.tempInFahrenheit
            binding.weatherMinTextView.text = it.minTempInFahrenheit
            binding.weatherMaxTextView.text = it.maxTempInFahrenheit
            binding.weatherFeelsTextView.text = it.feelsLikeTempInFahrenheit
            binding.weatherDescTextView.text = it.weatherDesc
            binding.cityTextView.text = it.cityName
        } ?: run {
            //show error message
        }
    }
}
