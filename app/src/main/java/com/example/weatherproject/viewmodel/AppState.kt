package com.example.weatherproject.viewmodel

import com.example.weatherproject.model.Weather

sealed class AppState {
    data class Success(val weatherData : List<Weather>) : AppState()
    data class Error(val error : Any): AppState()
    object Loading : AppState()
}