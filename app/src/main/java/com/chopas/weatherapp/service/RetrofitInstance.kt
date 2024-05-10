package com.chopas.weatherapp.service

import com.chopas.weatherapp.Utils.APIUtils
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


class RetrofitInstance {
    companion object {
        private val retrofitInstance by lazy {
            val okHttpClient = OkHttpClient.Builder().build()

            Retrofit.Builder().baseUrl(APIUtils.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()
        }

        val api: Service by lazy {
            retrofitInstance.create(Service::class.java)
        }
    }
}