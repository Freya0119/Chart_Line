package com.example.chart.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.widget.TextView
import com.example.chart.R
import com.example.chart.data.colors
import com.example.chart.data.dateColor
import com.example.chart.data.priceColor
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IDataSet

class CustomMarker(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {

    private val textView: TextView? = findViewById<TextView>(R.id.one_text_view)

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        val value = e?.x ?: 0f

        val year = FIRST_YEAR + (value / 12).toInt()
        val month = FIRST_MONTH + (value % 12).toInt()

        textView?.text = "${year}/${month}, 股價: ${e?.y}"

        super.refreshContent(e, highlight)
    }
}