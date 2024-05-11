package com.chopas.weatherapp

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chopas.weatherapp.adapter.SuggestionAdapter
import com.chopas.weatherapp.databinding.ActivityMainBinding
import com.chopas.weatherapp.model.SuggestionData
import com.chopas.weatherapp.model.WeatherData
import com.chopas.weatherapp.viewmodel.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var suggestionAdapter: SuggestionAdapter

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
        setViewListeners()
        loadCitySuggestions()

        loadWeather()
    }

    private fun loadCitySuggestions() {
        val suggestionList = arrayListOf<SuggestionData>(SuggestionData(R.drawable.baseline_my_location_24, getString(R.string.search_view_suggestion_text)))
        suggestionAdapter = SuggestionAdapter(suggestionList)

        binding.layoutSearchSuggestion.suggestionRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.layoutSearchSuggestion.suggestionRecyclerView.adapter = suggestionAdapter

        suggestionAdapter.onItemClick = {
            fetchLocation()
        }
    }

    private fun loadWeather() {
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
                binding.layoutDefaultMissingLocation.root.visibility = View.VISIBLE
                binding.weatherInfoLinearLayout.visibility = View.GONE
                val snackbar = Snackbar.make(binding.root, R.string.permission_denied_message, Snackbar.LENGTH_SHORT)
                snackbar.show()
            }
        }
    }

    private fun setViewModelListeners() {
        weatherViewModel.weatherDataMutableLiveData.observe(this) { weatherData ->
            updateViews(weatherData)
        }

        weatherViewModel.progressBarLiveData.observe(this) { isLoading ->
            if (isLoading) {
                binding.loadingLinearLayout.visibility = View.VISIBLE
            }
        }

        weatherViewModel.errorLiveData.observe(this) {
            GlobalScope.launch(Dispatchers.Main) {
                binding.citySearchView.clearFocus()

                binding.layoutSearchSuggestion.root.visibility = View.GONE
                binding.loadingLinearLayout.visibility = View.GONE

                val snackbar = Snackbar.make(binding.root, R.string.unable_to_find_information_message, Snackbar.LENGTH_SHORT)
                snackbar.show()
            }
        }
    }

    private fun setViewListeners() {
        binding.citySearchView.setOnQueryTextListener(object: OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.layoutSearchSuggestion.root.visibility = View.GONE

                query?.let { city ->
                    binding.citySearchView.clearFocus()

                    GlobalScope.launch(Dispatchers.IO) {
                        weatherViewModel.getWeatherByCity(city)
                    }
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.layoutSearchSuggestion.root.visibility = View.VISIBLE
                return false
            }
        })

        binding.citySearchView.setOnQueryTextFocusChangeListener { _, p1 ->
            if (p1) {
                binding.layoutSearchSuggestion.root.visibility = View.VISIBLE
            } else {
                binding.layoutSearchSuggestion.root.visibility = View.GONE
            }
        }

        binding.weatherInfoLinearLayout.setOnClickListener {
            binding.citySearchView.clearFocus()
        }
    }

    private fun updateViews(weatherData: WeatherData?) {
        weatherData?.let {
            GlobalScope.launch(Dispatchers.Main) {
                Glide.with(this@MainActivity)
                    .load(it.weatherIcon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.weatherIconImageView)

                binding.dateTimeTextView.text = it.dateTime
                binding.weatherFahrenheitTextView.text = it.tempInFahrenheit
                binding.weatherMinTextView.text = it.minTempInFahrenheit
                binding.weatherMaxTextView.text = it.maxTempInFahrenheit
                binding.weatherFeelsTextView.text = it.feelsLikeTempInFahrenheit
                binding.weatherDescTextView.text = it.weatherDesc
                binding.cityTextView.text = it.cityName
                binding.layoutSunriseSunset.sunriseTextView.text = it.sunriseTime
                binding.layoutSunriseSunset.sunsetTextView.text = it.sunsetTime
                binding.citySearchView.setQuery(it.cityName, false)
                binding.citySearchView.clearFocus()

                binding.layoutDefaultMissingLocation.root.visibility = View.GONE
                binding.weatherInfoLinearLayout.visibility = View.VISIBLE

                binding.layoutSearchSuggestion.root.visibility = View.GONE
                binding.loadingLinearLayout.visibility = View.GONE
            }
        }
    }
}
