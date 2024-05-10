package com.chopas.weatherapp.network

import com.chopas.weatherapp.utils.APIUtils
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitInstance {
    companion object {
        private val retrofitInstance by lazy {
            val okHttpClient = OkHttpClient.Builder().build() // ignore logging interceptor for now

            Retrofit.Builder().baseUrl(APIUtils.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()
        }

        val api: Service by lazy {
            retrofitInstance.create(Service::class.java)
        }
    }
}