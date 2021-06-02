package com.example.weatherproject.view.details

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.example.weatherproject.R
import com.example.weatherproject.WeatherDTO
import com.example.weatherproject.databinding.FragmentDetailsBinding
import com.example.weatherproject.model.Weather
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DetailsFragment : Fragment() {
    private var _binding : FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: Weather
    private val onLoadListener : WeatherLoader.WeatherLoaderListener = object : WeatherLoader.WeatherLoaderListener {
        override fun onFailed(throwable: Throwable) {
            displayErrorLoad()
        }
        override fun onLoaded(weatherDTO: WeatherDTO) {
            //displayErrorLoad()
            displayWeather(weatherDTO)
        }
    }

    companion object {
        const val BUNDLE_EXTRA = "weather"
        fun newInstance(bundle: Bundle) : DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.getRoot()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherBundle = arguments?.getParcelable<Weather>(BUNDLE_EXTRA) ?: Weather()
        binding.mainView.visibility = View.GONE
        binding.loadingLayout.visibility = View.VISIBLE
        val loader = WeatherLoader(onLoadListener, weatherBundle.city.lat, weatherBundle.city.lon)
        loader.loadWeather()
    }

    private fun displayWeather(weatherDTO : WeatherDTO) {
        with(binding) {
            mainView.visibility = View.VISIBLE
            loadingLayout.visibility = View.GONE
            val city = weatherBundle.city
            cityName.text = city.city
            cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                city.lat.toString(),
                city.lon.toString()
            )
            weatherCondition.text = weatherDTO.fact?.condition
            temperatureValue.text = weatherDTO.fact?.temp.toString()
            feelsLikeValue.text = weatherDTO.fact?.feels_like.toString()
        }
    }

    private fun displayErrorLoad() {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle("ERROR")
                .setMessage("Непонятная проблема")
                .setPositiveButton("Назад") {
                    dialog, id ->  dialog.cancel()
                    fragmentManager?.popBackStack()
                }
        dialogBuilder.create()
        dialogBuilder.show()
    }

}
