package com.example.chart

import com.example.chart.data.StockData
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://api.nstock.tw/"

interface ServiceApi {
    @GET("v2/per-river/interview")
    fun getStockResult(@Query("stock_id") stockId: Int): Call<StockData>

    companion object {
        val getter: ServiceApi by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(ServiceApi::class.java)
        }
    }
}