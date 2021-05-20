package com.example.weatherproject.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.weatherproject.R
import com.example.weatherproject.databinding.MainFragmentBinding
import com.example.weatherproject.model.Weather
import com.example.weatherproject.viewmodel.AppState
import com.example.weatherproject.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var _binding : MainFragmentBinding? = null
    private val binding get() = _binding!!



    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.getRoot()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getWeatherFromLocalSource()
    }

    private fun renderData(appState: AppState){
        when(appState) {
            is AppState.Success -> {
                val weather = appState.weatherData
                binding.loadingLayout.visibility = View.GONE
                //Snackbar.make(binding.mainView, "Success", Snackbar.LENGTH_LONG).show()
                setData(weather)
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.VISIBLE
                Snackbar.make(binding.mainView, "Ошибка", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") {viewModel.getWeatherFromLocalSource()}
                    .show()
            }

        }
    }

    private fun setData(weatherData: Weather) {
        binding.cityName.text = weatherData.city.city
        binding.cityCoordinates.text = String.format(
            getString(R.string.city_coordinates),
            weatherData.city.lat.toString(),
            weatherData.city.lon.toString()
        )
        binding.temperatureValue.text = weatherData.city.temp.toString()
        binding.feelsLikeValue.text = weatherData.city.feelLike.toString()
    }

}