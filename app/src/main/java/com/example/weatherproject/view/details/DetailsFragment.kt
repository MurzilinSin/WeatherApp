package com.example.weatherproject.view.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.weatherproject.R
import com.example.weatherproject.databinding.FragmentDetailsBinding
import com.example.weatherproject.model.City
import com.example.weatherproject.model.Weather
import com.example.weatherproject.utils.showSnackBar
import com.example.weatherproject.viewmodel.AppState
import com.example.weatherproject.viewmodel.DetailsViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment() {

    private var _binding : FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: Weather
    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherBundle = arguments?.getParcelable<Weather>(BUNDLE_EXTRA) ?: Weather()
       // getWeather()
        viewModel.detailsLiveData.observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getWeatherFromRemoteSource(weatherBundle.city.lat,weatherBundle.city.lon)
    }


    private fun renderData(appState : AppState) {
        when(appState) {
            is AppState.Success -> {
                binding.mainView.visibility = View.VISIBLE

                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                setWeather(appState.weatherData[0])
            }
            is AppState.Loading -> {
                binding.mainView.visibility = View.GONE
                binding.includedLoadingLayout.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.mainView.visibility = View.VISIBLE
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                binding.mainView.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload),
                    {viewModel.getWeatherFromRemoteSource(weatherBundle.city.lat, weatherBundle.city.lon)}
                )
            }
        }
    }

    private fun setWeather(weather: Weather){
        val city = weatherBundle.city
        saveCity(city,weather)
        binding.cityName.text = city.city
        binding.cityCoordinates.text = String.format(
            getString(R.string.city_coordinates),
            city.lat.toString(),
            city.lon.toString()
        )
        binding.temperatureValue.text = weather.temp.toString()
        binding.feelsLikeValue.text = weather.feelsLike.toString()
        binding.weatherCondition.text = weather.condition

        Picasso
            .get()
            .load("https://www.pngkey.com/png/full/217-2172851_cartoon-city-png-svg-download-cartoon-cities.png")
            .into(headerIcon)

        Glide
                .with(this)
                .load("https://www.pngkey.com/png/full/360-3605944_green-grass-ground-png-clip-cartoon-grass-field.png")
                .into(footerIcon)
    }

    private fun saveCity(city: City, weather: Weather) {
        viewModel.saveCityToDB(
            Weather(city, weather.temp, weather.feelsLike,weather.condition)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val BUNDLE_EXTRA = "weather"
        fun newInstance(bundle: Bundle) : DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
