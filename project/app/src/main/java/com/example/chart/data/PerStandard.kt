package com.example.chart.data

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class PerStandard(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo("股票代號")
    val stockCode: Int,

    @ColumnInfo
    val value: Float
)