package com.example.weatherproject.view.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherproject.R
import com.example.weatherproject.model.Weather

class MainFragmentAdapter (private var onItemViewClickListener: MainFragment.OnItemViewClickListener?)
    : RecyclerView.Adapter<MainFragmentAdapter.MainViewHolder>() {
    private var weatherData : List<Weather> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            MainViewHolder {
        return MainViewHolder(LayoutInflater.from(parent.context).
        inflate(R.layout.fragment_main_recycler_item,parent,false) as View)
    }

    override fun getItemCount(): Int {
        return weatherData.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
       holder.bind(weatherData[position])
    }

    fun setWeather(data: List<Weather>) {
        weatherData = data
        notifyDataSetChanged()
    }

    fun removeListener() {
        onItemViewClickListener = null
    }

    inner class MainViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        fun bind(weather: Weather) {
            itemView.apply {
                findViewById<TextView>(R.id.mainFragmentRecyclerItemTextView).text =
                        weather.city.city
                setOnClickListener { onItemViewClickListener?.onItemViewClick(weather) }
            }
        }
    }

}