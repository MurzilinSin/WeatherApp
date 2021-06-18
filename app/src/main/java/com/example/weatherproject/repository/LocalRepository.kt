package com.example.weatherproject.repository

import com.example.weatherproject.model.Weather

interface LocalRepository {
    fun getAllHistory(): List<Weather>
    fun saveEntity(weather: Weather)
}