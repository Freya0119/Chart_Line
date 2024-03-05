package com.example.chart.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("本淨比基準")
data class PbrStandard(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo("股票代號")
    val stockCode: Int,

    @ColumnInfo
    val value: Float
)