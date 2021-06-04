package com.example.weatherproject.view

import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weatherproject.R
import com.example.weatherproject.databinding.MainActivityBinding
import com.example.weatherproject.view.main.MainBroadcast
import com.example.weatherproject.view.main.MainFragment

private val receiver = MainBroadcast()

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.container,
                        MainFragment.newInstance()
                    )
                    .commitNow()
        }
        registerReceiver(receiver, IntentFilter(CONNECTIVITY_SERVICE))
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }
}