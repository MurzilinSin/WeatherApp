package com.example.weatherproject.model

data class Weather(
    val city: City = getDefaultCity()
    /*val temp : Int = 0,
    val feelsLike : Int = 0*/
)

fun getDefaultCity() = City("Дубна", 23.444, 12312.213123,24,32)