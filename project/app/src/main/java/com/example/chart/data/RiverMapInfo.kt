package com.example.chart.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class RiverMapInfo(
    @ColumnInfo("平均本淨比")
    @SerializedName("平均本淨比")
    val pbrAverage: String?,

    @ColumnInfo("平均本益比")
    @SerializedName("平均本益比")
    val perAverage: String?,

    @ColumnInfo("年月")
    @SerializedName("年月")
    val yearMonth: String?,

    @ColumnInfo("月平均收盤價")
    @SerializedName("月平均收盤價")
    val closingPriceMonth: Float?,

    @ColumnInfo("月近一季本淨比")
    @SerializedName("月近一季本淨比")  // 本淨比（Price-to-Book Ratio, PB ratio）
    val pbrQuarter: String?,

    @ColumnInfo("近3年年複合成長")
    @SerializedName("近3年年複合成長") // Compound Annual Growth Rate（簡稱CAGR）
    val cagrThree: String?,

    @ColumnInfo("近一季BPS")
    @SerializedName("近一季BPS")   // 每股淨值（BPS）
    val bpsQuarter: String?,

    @ColumnInfo("近四季EPS")
    @SerializedName("近四季EPS")   // 每股盈餘(EPS)
    val epsYear: Float?,

    @ColumnInfo("月近四季本益比")
    @SerializedName("月近四季本益比")  // 本益比 (Price-to-Earnings Ratio, PE ratio)
    val perYear: String?,

    @SerializedName("本淨比股價基準")
    val pbrStockPriceStandard: List<Float?>?,

    @SerializedName("本益比股價基準")
    val perStockPriceStandard: List<Float?>?
)