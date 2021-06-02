package com.example.weatherproject.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherproject.R
import com.example.weatherproject.databinding.FragmentMainBinding
import com.example.weatherproject.model.Weather
import com.example.weatherproject.view.details.DetailsFragment
import com.example.weatherproject.viewmodel.AppState
import com.example.weatherproject.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
//0030c3ee-b406-44b4-910b-494047839e07 ключ API
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private var isDataSetRus: Boolean = true
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private val adapter = MainFragmentAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            activity?.supportFragmentManager?.apply {
                beginTransaction()
                        .replace(R.id.container, DetailsFragment.newInstance(Bundle().apply {
                            putParcelable(DetailsFragment.BUNDLE_EXTRA, weather)
                        }))
                        .addToBackStack("")
                        .commitAllowingStateLoss()
            }
        }
    })

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainFragmentRecyclerView.adapter = adapter
        binding.mainFragmentFAB.setOnClickListener { changeWeatherDataSet() }
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            renderData(it) })
        viewModel.getWeatherFromLocalSourceRus()
    }

    private fun changeWeatherDataSet() {
        if (isDataSetRus) {
            viewModel.getWeatherFromLocalSourceWorld()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
        } else {
            viewModel.getWeatherFromLocalSourceRus()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
        }.also { isDataSetRus = !isDataSetRus }
    }

    private fun renderData(appState: AppState) {

        when (appState) {
            is AppState.Success -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                binding.mainFragmentRecyclerView.visibility = View.VISIBLE
                adapter.setWeather(appState.weatherData)
            }
            is AppState.Loading -> {
                binding.mainFragmentLoadingLayout.visibility = View.VISIBLE
                binding.mainFragmentFAB.snackBar("", 1000, R.color.colorAccent)
            }
            is AppState.Error -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                binding.mainFragmentRootView.showSnackBar(
                        getString(R.string.error),
                        getString(R.string.reload),
                        {viewModel.getWeatherFromLocalSourceRus()})
            }
        }
    }

    private fun View.showSnackBar (text : String, actionText : String, action : (View) -> Unit,length: Int = Snackbar.LENGTH_INDEFINITE) {
        Snackbar.make(this,text,length).setAction(actionText,action).show()
    }

    private fun View.snackBar(text: String, length: Int = Snackbar.LENGTH_INDEFINITE, color: Int){
        Snackbar.make(this,text,length).setTextColor(color)
    }

    private fun Snackbar.print(){}

    companion object {
        fun newInstance() = MainFragment()
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(weather: Weather)
    }

    override fun onDestroy() {
        adapter.removeListener()
        super.onDestroy()
    }
}
