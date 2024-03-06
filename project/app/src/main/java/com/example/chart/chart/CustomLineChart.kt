package com.example.chart.chart

import android.content.Context
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis

class CustomLineChart(context: Context) : LineChart(context) {
    fun setCustom() {
        this.isScaleXEnabled = true
        this.isScaleYEnabled = false

        this.xAxis.position = XAxis.XAxisPosition.BOTTOM
    }

    fun setRange(min: Float) {
        this.xAxis.axisMinimum = min
    }
}