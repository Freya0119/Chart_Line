package com.example.chart.chart

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.DefaultFillFormatter
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class CustomFillFormatter : IFillFormatter {
    private var boundaryDataSet: ILineDataSet?

    internal constructor(boundaryDataSet: ILineDataSet?) {
        this.boundaryDataSet = boundaryDataSet
    }

    fun getFillLineBoundary(): List<Entry>? {
        if (boundaryDataSet != null) {
            val dataSet = boundaryDataSet as LineDataSet
            return dataSet.values
        } else {
            return null
        }
    }

    override fun getFillLinePosition(dataSet: ILineDataSet, dataProvider: LineDataProvider): Float {
        return 0f
    }
}
