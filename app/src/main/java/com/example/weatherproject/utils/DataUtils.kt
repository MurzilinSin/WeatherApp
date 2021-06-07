package com.example.weatherproject.utils

import com.example.weatherproject.FactDTO
import com.example.weatherproject.WeatherDTO
import com.example.weatherproject.model.Weather
import com.example.weatherproject.model.getDefaultCity

fun convertDtoToModel(weatherDTO: WeatherDTO) : List<Weather> {
    val fact: FactDTO = weatherDTO.fact!!
    return listOf(Weather(getDefaultCity(),fact.temp!!,fact.feels_like!!,fact.condition!!/*,fact.icon*/))
}