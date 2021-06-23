package com.example.weatherproject.view.main

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherproject.R
import com.example.weatherproject.databinding.FragmentMainBinding
import com.example.weatherproject.model.City
import com.example.weatherproject.model.Weather
import com.example.weatherproject.view.details.DetailsFragment
import com.example.weatherproject.viewmodel.AppState
import com.example.weatherproject.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_main.*
import java.io.IOException


private const val IS_WORLD_KEY = "LIST_OF_TOWNS_KEY"
private const val REFRESH_PERIOD = 60000L
private const val MINIMAL_DISTANCE = 100f
private const val REQUEST_CODE = 12345

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private var isDataSetWorld: Boolean = false
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private val adapter = MainFragmentAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            openDetailsFragment(weather)
        }
    })
    private val onLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            context?.let {
                getAddressAsync(it,location)
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

        }
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainFragmentRecyclerView.adapter = adapter
        binding.mainFragmentFAB.setOnClickListener { changeWeatherDataSet() }
        binding.mainFragmentFABLocation.setOnClickListener { checkPermission() }
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            renderData(it) })
        showListOfTowns()
    }

    private fun checkPermission() {
        activity?.let {
            when{
                ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED -> {
                            getLocation()
                        }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showRationaleDialog()
                }
                else -> {
                    requestPermission()
                }
            }
        }
    }

    private fun getLocation() {
        activity?.let { context ->
            if(ContextCompat.checkSelfPermission(
                            context,Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                var locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    val provider =
                            locationManager.getProvider(LocationManager.GPS_PROVIDER)
                    provider?.let {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                REFRESH_PERIOD,
                                MINIMAL_DISTANCE,
                                onLocationListener
                        )
                    }
                } else {
                    val location =
                            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (location == null) {
                        showDialog(
                                getString(R.string.dialog_title_gps_turned_off),
                                getString(R.string.dialog_message_last_known_location)
                        )
                    } else {
                        getAddressAsync(context, location)
                        showDialog(
                                getString(R.string.dialog_title_gps_turned_off),
                                getString(R.string.dialog_message_last_known_location)
                        )
                    }
                }

            } else {
                showRationaleDialog()
            }
        }
    }

    private fun getAddressAsync(context: Context, location: Location) {
        val geoCoder = Geocoder(context)
        Thread {
            try {
                val address = geoCoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1
                )
                mainFragmentFAB.post {
                    showAddressDialog(address[0].getAddressLine(0), location)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun showAddressDialog(address: String, location: Location) {
        activity?.let { 
            AlertDialog.Builder(it)
                    .setTitle(getString(R.string.dialog_address_title))
                    .setMessage(address)
                    .setPositiveButton(getString(R.string.dialog_address_get_weather))
                    {_,_ -> openDetailsFragment(
                            Weather(City(address,location.latitude,location.longitude))
                    )}
                    .setNegativeButton(getString(R.string.dialog_button_close))
                    {dialog, _ -> dialog.dismiss() }
                    .create()
                    .show()
        }
    }

    private fun openDetailsFragment(weather: Weather) {
        activity?.supportFragmentManager?.apply {
            beginTransaction()
                    .replace(R.id.container, DetailsFragment.newInstance(Bundle().apply {
                        putParcelable(DetailsFragment.BUNDLE_EXTRA, weather)
                    }))
                    .addToBackStack("")
                    .commitAllowingStateLoss()
        }
    }

    private fun showRationaleDialog() {
        activity?.let {
            AlertDialog.Builder(it)
                    .setTitle(getString(R.string.dialog_rationale_title))
                    .setMessage(getString(R.string.dialog_rationale_message))
                    .setPositiveButton(getString(R.string.dialog_rationale_give_access))
                    {_,_-> requestPermission()}
                    .setNegativeButton(getString(R.string.dialog_rationale_decline))
                    {dialog,_-> dialog.dismiss()}
                    .create()
                    .show()
        }
    }

    private fun requestPermission() {
        requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        checkPermissionsResult(requestCode, grantResults)
    }

    private fun checkPermissionsResult(requestCode: Int, grantResults: IntArray) {
        when(requestCode) {
            REQUEST_CODE -> {
                var grantedPermissions = 0
                if((grantResults.isNotEmpty())) {
                    for(i in grantResults) {
                        if(i == PackageManager.PERMISSION_GRANTED) {
                            grantedPermissions++
                        }
                    }
                    if(grantResults.size == grantedPermissions) {
                        getLocation()
                    }
                    else {
                        showDialog(
                                getString(R.string.dialog_title_no_gps),
                                getString(R.string.dialog_message_no_gps)
                        )
                    }
                } else {
                    showDialog(
                            getString(R.string.dialog_title_no_gps),
                            getString(R.string.dialog_message_no_gps)
                    )
                }
                return
            }
        }
    }

    private fun showDialog(title: String, message: String) {
        activity?.let {
            AlertDialog.Builder(it)
                    .setTitle(title)
                    .setMessage(message)
                    .setNegativeButton(getString(R.string.dialog_button_close))
                    { dialog, _ -> dialog.dismiss() }
                    .create()
                    .show()
        }
    }

    private fun showListOfTowns() {
        activity?.let {
            if(it.getPreferences(Context.MODE_PRIVATE).getBoolean(IS_WORLD_KEY,false)){
                changeWeatherDataSet()
            } else {
                viewModel.getWeatherFromLocalSourceRus()
            }
        }
    }



    private fun changeWeatherDataSet() {
        if (isDataSetWorld) {
            viewModel.getWeatherFromLocalSourceRus()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
        } else {
            viewModel.getWeatherFromLocalSourceWorld()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
        }.also {isDataSetWorld = !isDataSetWorld  }
        saveListOfTowns(isDataSetWorld)
    }

    private fun saveListOfTowns(dataSetWorld: Boolean) {
        activity?.let {
            with(it.getPreferences(Context.MODE_PRIVATE).edit()) {
                putBoolean(IS_WORLD_KEY, isDataSetWorld)
                apply()
            }
        }
    }

    private fun renderData(appState: AppState) {

        when (appState) {
            is AppState.Success -> {
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                binding.mainFragmentRecyclerView.visibility = View.VISIBLE
                adapter.setWeather(appState.weatherData)
            }
            is AppState.Loading -> {
                binding.includedLoadingLayout.loadingLayout.visibility = View.VISIBLE
                binding.mainFragmentFAB.snackBar("", 1000, R.color.colorAccent)
            }
            is AppState.Error -> {
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
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
