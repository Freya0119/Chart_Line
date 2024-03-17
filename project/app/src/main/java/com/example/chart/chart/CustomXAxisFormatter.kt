package com.example.chart.chart

import android.util.Log
import com.example.chart.DATA_MSG
import com.github.mikephil.charting.formatter.ValueFormatter

class CustomXAxisFormatter : ValueFormatter {
    private var labelStr: List<String>?

    internal constructor(strList: List<String>) {
        this.labelStr = strList
    }

    override fun getFormattedValue(value: Float): String {
        return labelStr?.get(value.toInt()) ?: "No value"
    }
}