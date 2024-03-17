package com.example.chart

import android.app.LocaleConfig
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

const val DATA_MSG = "DATA_MSG"

const val STOCK_ID = 2330

class MainActivity : AppCompatActivity() {
    // TODO: init viewModel lifeCycle
    private val viewModel by viewModels<DataViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)
    }
}