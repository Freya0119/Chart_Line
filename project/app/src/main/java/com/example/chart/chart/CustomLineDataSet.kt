package com.example.chart.chart

import android.graphics.Canvas
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.renderer.LineChartRenderer
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.ViewPortHandler

class CustomLineDataSet(entry: List<Entry>, label: String) : LineDataSet(entry, label) {
    fun setCircle() {
        this.setDrawCircles(false)
    }

    fun setLineColor(color: Int, alpha: Int, isFill: Boolean) {
        this.color = color
        this.fillColor = color
        this.fillAlpha = 255

        this.setDrawFilled(isFill)
    }
}