package com.example.chart.chart

import android.content.Context
import android.util.Log
import com.example.chart.DATA_MSG
import com.example.chart.DataViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.ValueFormatter
import java.util.Date

const val FIRST_YEAR = 2018
const val FIRST_MONTH = 2

class CustomLineChart(context: Context) : LineChart(context) {
    fun setCustom() {
        this.isScaleXEnabled = true
        this.isScaleYEnabled = false

        this.xAxis.position = XAxis.XAxisPosition.BOTTOM
    }

    fun setXAXis() {
        this.xAxis.textSize = 3f

        val formatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                val year = FIRST_YEAR + (value / 12).toInt()
                val month = FIRST_MONTH + (value % 12).toInt()
                return "$year.$month"
            }
        }
        this.xAxis.valueFormatter = formatter
    }

    fun setRange(min: Float) {
        this.xAxis.axisMinimum = min
    }
}