package com.example.chart.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class StockData(
    @SerializedName("data")
    val dataList: List<Stock>
)

@Entity("single_stock")
data class Stock(
    @ColumnInfo("同業本淨比中位數")
    @SerializedName("同業本淨比中位數")
    val pbrPeerCenter: String?,

    @ColumnInfo("同業本益比中位數")
    @SerializedName("同業本益比中位數")
    val perPeerCenter: String?,

    @ColumnInfo("平均本淨比")
    @SerializedName("平均本淨比")
    val pbrAverage: String?,

    @ColumnInfo("平均本益比")
    @SerializedName("平均本益比")
    val perAverage: String?,

    @ColumnInfo("本淨比股價評估")
    @SerializedName("本淨比股價評估")
    val pbrStockPriceAssessment: String?,

    @ColumnInfo("本益成長比")
    @SerializedName("本益成長比")
    val peg: String?,

    @ColumnInfo("本益比股價評估")
    @SerializedName("本益比股價評估")
    val perStockPriceAssessment: String?,

    @ColumnInfo("目前本淨比")
    @SerializedName("目前本淨比")
    val pbrNow: String?,

    @ColumnInfo("目前本益比")
    @SerializedName("目前本益比")
    val perNow: String?,

    @PrimaryKey
    @SerializedName("股票代號")
    val stockCode: Int?,

    @ColumnInfo("股票名稱")
    @SerializedName("股票名稱")
    val stockName: String?,

    @SerializedName("本淨比基準")
    val pbrStandard: List<String?>?,

    @SerializedName("本益比基準")
    val perStandard: List<String?>?,

    @SerializedName("河流圖資料")
    val riverMapInfo: List<RiverMapInfo?> = listOf()
)