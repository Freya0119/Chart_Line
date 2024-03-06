package com.example.chart.chart

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import com.example.chart.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight

val colors = listOf(
    Color.RED,
    Color.parseColor("#FF8000"),
    Color.parseColor("#FFFF00"),
    Color.parseColor("#80FF00"),
    Color.parseColor("#00FFFF"),
    Color.parseColor("#0000FF"),
    Color.parseColor("#FF00FF")
)

class CustomLineDataSet(entry: List<Entry>, label: String) : LineDataSet(entry, label) {
    fun setCircle() {
        this.setDrawCircles(false)
    }

    fun setLineColor(color: Int, isFill: Boolean) {
        this.color = color
        this.fillColor = color
        this.fillAlpha = 255

        this.setDrawFilled(isFill)
    }
}