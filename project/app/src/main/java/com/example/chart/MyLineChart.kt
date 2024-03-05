package com.example.chart

import android.content.Context
import android.graphics.Color
import android.graphics.DashPathEffect
import android.util.Log
import android.widget.TextView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils.init

class MyLineChart(context: Context) : LineChart(context) {
    init {
        mGridBackgroundPaint.color = Color.rgb(137, 230, 81)
    }
}

class MyMarker(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {
    private val textView: TextView? = findViewById<TextView>(R.id.one_text_view)

    private val legend1 = LegendEntry("set1", Legend.LegendForm.CIRCLE, 10f, 2f, null, Color.GREEN)

    private val legendArr = arrayListOf<LegendEntry>(legend1, legend1)

    fun setLegend() {
        val legend = chartView.legend
        legend.setCustom(legendArr)
    }

    override fun getOffsetForDrawingAtPoint(posX: Float, posY: Float): MPPointF {
        return super.getOffsetForDrawingAtPoint(posX, posY)
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)
        textView?.text = "X: ${e?.x}, Y: ${e?.y}"
        legendArr[0].label = "X: ${e?.x}, Y: ${e?.y}"
    }
}