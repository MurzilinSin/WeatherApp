package com.example.weatherproject.repository

import com.example.weatherproject.model.Weather
import com.example.weatherproject.model.getRussianCities
import com.example.weatherproject.model.getWorldCities

class RepositoryImpl : Repository {
    override fun getWeatherFromServer() = Weather()

    override fun getWeatherFromLocalStorageRus() = getRussianCities()

    override fun getWeatherFromLocalStorageWorld() = getWorldCities()

}