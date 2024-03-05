package com.example.chart.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("本益比股價基準")
class PerStockPriceStandard(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo("股票代號")
    val stockCode: Int,

    @ColumnInfo
    val value: Float

)