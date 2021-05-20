package com.example.weatherproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherproject.model.Repository
import com.example.weatherproject.model.RepositoryImpl
import com.example.weatherproject.model.Weather
import java.lang.Thread.sleep
import kotlin.random.Random

class MainViewModel(private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
private val repositoryImpl : Repository = RepositoryImpl()
                    ) : ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getWeatherFromLocalSource() = getDataFromLocalSource()

    fun getWeatherFromRemoteSource() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        val random: Int = Random.nextInt(1,4)
        Thread {
            sleep(1000)

            if(random%2 !=0) liveDataToObserve.postValue(AppState.Success(repositoryImpl.getWeatherFromLocalStorage()))
            else liveDataToObserve.postValue(AppState.Error(Any()))
        }.start()
    }
}
