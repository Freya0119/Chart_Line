package com.example.chart.chart

import android.content.Context
import android.widget.TextView
import com.example.chart.R
import com.example.chart.data.colors
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight

class CustomMarker(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {
    private val textView: TextView? = findViewById<TextView>(R.id.one_text_view)

    private val lineCount = 7

    private val formSize = 4f
    private val formLineWidth = 1f

    private lateinit var labels: List<String>
    private lateinit var legendList: MutableList<LegendEntry>

    init {
        setLabel()
    }

    fun setLegends() {
        chartView.legend?.resetCustom()

        val lenList = MutableList(lineCount) {    // 股價
            LegendEntry(
                labels[it],
                Legend.LegendForm.DEFAULT,
                formSize,
                formLineWidth,
                null,
                colors[it]
            )
        }
        legendList = lenList
        chartView.legend?.setCustom(legendList)
    }

    fun setLegendPosition() {
        chartView.legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        chartView.legend.orientation = Legend.LegendOrientation.HORIZONTAL

        chartView.legend.xOffset = 0f
        chartView.legend.yOffset = 0f

        chartView.legend.textSize = 8f
        chartView.legend.formSize = 8f

        chartView.legend.xEntrySpace = 2f
        chartView.legend.yEntrySpace = 2f
        chartView.legend.formToTextSpace = 2f
        chartView.legend.isWordWrapEnabled = true
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        val value = e?.x ?: 0f

        val year = FIRST_YEAR + (value / 12).toInt()
        val month = FIRST_MONTH + (value % 12).toInt()

        textView?.text = "${year}/${month}, 股價: ${e?.y}"

        if (legendList.size == lineCount) { // 最後一個是股價
            for (i in 0..<lineCount) {
                if (e != null) {
                    val yOffset = chartView.data.dataSets[i].getEntryForIndex(e.x.toInt()).y
                    legendList[i].label = "Y: $yOffset"
                }
            }
        }
        super.refreshContent(e, highlight)
    }

    private fun setLabel() {
        val list = List<String>(lineCount) {
            "Label $it"
        }
        labels = list
    }
}