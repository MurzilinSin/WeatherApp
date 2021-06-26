package com.example.weatherproject.repository

import com.example.weatherproject.model.Weather
import com.example.weatherproject.room.HistoryDao
import com.example.weatherproject.utils.*

class LocalRepositoryImpl(private val localDataSource: HistoryDao): LocalRepository {
    override fun getAllHistory(): List<Weather> {
        return convertHistoryEntityToWeather(localDataSource.all())
    }

    override fun saveEntity(weather: Weather) {
        localDataSource.insert(convertWeatherToEntity(weather))
    }
}