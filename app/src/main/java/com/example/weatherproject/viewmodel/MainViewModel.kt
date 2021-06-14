package com.example.weatherproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherproject.repository.Repository
import com.example.weatherproject.repository.RepositoryImpl
import kotlin.random.Random

class MainViewModel(private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
private val repositoryImpl : Repository = RepositoryImpl()
                    ) : ViewModel() {

    fun getLiveData() = liveDataToObserve
    fun getWeatherFromLocalSourceRus() = getDataFromLocalSource(isRussian = true)
    fun getWeatherFromLocalSourceWorld() = getDataFromLocalSource(isRussian = false)
    fun getWeatherFromRemoteSource() = getDataFromLocalSource(isRussian = true)

    private fun getDataFromLocalSource(isRussian : Boolean) {
        val random: Int = Random.nextInt(1,4)
        liveDataToObserve.value = AppState.Loading
        Thread {
            //sleep(500)
            liveDataToObserve.postValue(AppState.Success(if (isRussian)
                repositoryImpl.getWeatherFromLocalStorageRus() else
                repositoryImpl.getWeatherFromLocalStorageWorld()))
        }.start()
    }
}
