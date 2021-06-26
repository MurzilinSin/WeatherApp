package com.example.weatherproject.view.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import java.lang.StringBuilder

class MainBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        StringBuilder().apply {
            append("Домашнее задание номер 6\n")
            append("Действие: ${intent?.action}")
            toString().also {
                Toast.makeText(context,it, Toast.LENGTH_LONG).show()
            }
        }
    }
}