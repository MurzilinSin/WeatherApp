package com.example.weatherproject.repository

import com.example.weatherproject.WeatherDTO
import retrofit2.Callback


interface DetailsRepository {
    fun getWeatherDetailsFromServer(
        lat: Double,
        lon: Double,
        callback: Callback<WeatherDTO>
    )
}