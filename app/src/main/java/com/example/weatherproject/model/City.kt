package com.example.weatherproject.model

data class City (
    val city : String,
    val lat: Double,
    val lon: Double,
    val temp : Int,
    val feelLike : Int
)