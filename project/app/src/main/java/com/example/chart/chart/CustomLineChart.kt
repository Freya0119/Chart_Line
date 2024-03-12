package com.example.chart.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.util.Log
import android.view.MotionEvent
import com.example.chart.DATA_MSG
import com.example.chart.DataViewModel
import com.example.chart.data.colors
import com.example.chart.data.dateColor
import com.example.chart.data.priceColor
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.renderer.LegendRenderer
import java.util.Date

const val FIRST_YEAR = 2018
const val FIRST_MONTH = 2

class CustomLineChart(context: Context) : LineChart(context) {

    private val formSize = 6f
    private val formLineWidth = 1f

    private lateinit var legendList: MutableList<LegendEntry>

    fun setCustom() {
        this.isScaleXEnabled = true
        this.isScaleYEnabled = false

        setListener()
    }

    fun setXAXis() {
        this.xAxis.textSize = 3f

        this.xAxis.position = XAxis.XAxisPosition.BOTTOM

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

    private fun setLabels(e: Entry) {
        if (legendList.size > 0) {
            val value = e.x

            val year = FIRST_YEAR + (value / 12).toInt()
            val month = FIRST_MONTH + (value % 12).toInt()

            val dateStr = "${year}/${month}"
            val priceStr = "${this.data.dataSets[6].getY(e)}"
            val riverStrings = getRiver(e)

            setLegendList(dateStr, priceStr, riverStrings)
        }
    }

    private fun setLegendList(date: String, price: String, river: List<String>) {
        legendList[0].label = "日期: $date"
        legendList[1].label = "股價: $price"
        river.forEachIndexed { index, s ->
            legendList[2 + index].label = String.format("Y: %-25s", s)
        }
        notifyDataSetChanged()
    }

    private fun getRiver(e: Entry): List<String> {
        // chartView.data.dataSets 0~5: river, 6: price,
        // legend 0: date, 1: price, other: river
        val list = mutableListOf<String>()

        for (i in 2..legendList.lastIndex) {
            list.add("${this.data.dataSets[i - 2].getY(e)}")
        }
        return list
    }

    private fun IDataSet<out Entry>.getY(entry: Entry): Float {
        return getEntryForIndex(entry.x.toInt()).y
    }

    fun setLegends() {
        legendList = mutableListOf()

        addDateLegend()
        addPriceLegend()
        addRiverLegend()

        this.legend?.resetCustom()
        this.legend?.setCustom(legendList)
    }

    private fun addDateLegend() {
        legendList.add(
            LegendEntry(
                "日期",
                Legend.LegendForm.NONE,
                0f,
                0f,
                null,
                dateColor
            )
        )
    }

    private fun addPriceLegend() {
        legendList.add(
            LegendEntry(
                "股價",
                Legend.LegendForm.DEFAULT,
                formSize,
                formLineWidth,
                null,
                priceColor
            )
        )
    }

    private fun addRiverLegend() {
        for (i in 0..colors.lastIndex) {
            legendList.add(
                LegendEntry( // price
                    "河流: $i",
                    Legend.LegendForm.DEFAULT,
                    formSize,
                    formLineWidth,
                    null,
                    colors[i]
                )
            )
        }
    }

    fun setLegendPosition() {
        this.legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        this.legend.orientation = Legend.LegendOrientation.HORIZONTAL

        this.legend.xOffset = 0f
        this.legend.yOffset = 0f

        this.legend.textSize = 10f
        this.legend.formSize = 10f

        this.legend.xEntrySpace = 16f
        this.legend.yEntrySpace = 1f
        this.legend.formToTextSpace = 2f
        this.legend.isWordWrapEnabled = true
    }

    private fun setListener() {
        this.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (e != null) {
                    setLabels(e)
                }
            }

            override fun onNothingSelected() {
                Log.d(DATA_MSG, "Nothing selected")
            }
        })
    }
}