package com.example.weatherproject.repository

import com.example.weatherproject.WeatherDTO
import retrofit2.Callback


class DetailsRepositoryImpl (private val remoteDataSource: RemoteDataSource) : DetailsRepository {
    override fun getWeatherDetailsFromServer(
        lat: Double,
        lon: Double,
        callback: Callback<WeatherDTO>
    ) {
        remoteDataSource.getWeatherDetails(lat,lon,callback)
    }
}