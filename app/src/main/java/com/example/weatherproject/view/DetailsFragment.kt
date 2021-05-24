package com.example.weatherproject.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherproject.R
import com.example.weatherproject.databinding.FragmentDetailsBinding
import com.example.weatherproject.model.Weather
import com.example.weatherproject.viewmodel.MainViewModel


class DetailsFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var _binding : FragmentDetailsBinding? = null
    private val binding get() = _binding!!

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weather = arguments?.getParcelable<Weather>(BUNDLE_EXTRA)
        if(weather != null) {
            val city = weather.city
            binding.cityName.text = city.city
            binding.cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                city.lat.toString(),
                city.lon.toString()
            )
            binding.temperatureValue.text = weather.temp.toString()
            binding.feelsLikeValue.text = weather.feelsLike.toString()
        }
    }

}
