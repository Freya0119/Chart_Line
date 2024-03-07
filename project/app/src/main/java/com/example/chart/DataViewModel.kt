package com.example.chart

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chart.chart.CustomLineDataSet
import com.example.chart.data.Stock
import com.example.chart.data.StockData
import com.example.chart.data.colors
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.Utils.init
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataViewModel() : ViewModel() {
    val dataList = MutableLiveData<List<Stock>?>(emptyList())

    val dataSets = MutableLiveData<List<ILineDataSet>>()

    init {
        initList()
        getStock()
    }

    private fun getEntry(stockId: Int) {
        val riverMap = dataList.value?.find { it.stockCode == stockId }?.riverMapInfo
        if (riverMap != null) {
            val entryLists = mutableListOf<List<Entry>>()

            val newRiver = riverMap.sortedBy { it?.yearMonth }

            val price = newRiver.map { it?.closingPriceMonth ?: 0f }    // 股價 -> y軸  // 倒過來，軸依賴

            val indexList = MutableList(price.size) { it.toFloat() }

            val priceEntryList = indexList.zip(price) { x, y -> Entry(x.toFloat(), y) }

            // 河流圖
            val perSize = 6 - 1
            for (i in perSize downTo 0) {
                val perList = newRiver.map { it?.perStockPriceStandard?.get(i) ?: 0f }
                val entryList = indexList.zip(perList) { x, y -> Entry(x, y) }
                entryLists.add(entryList)
            }
            entryLists.add(priceEntryList)

            setLineDataSets(entryLists)
        }
    }

    private fun setLineDataSets(entryList: List<List<Entry>>) {
        val sets = mutableListOf<ILineDataSet>()

        // price data
        val priceData = CustomLineDataSet(entryList[6], "Price")
        priceData.setLineColor(colors[6], false)
        priceData.setCircle()   // false

        for (i in 0..<entryList.size - 1) {
            val data = CustomLineDataSet(entryList[i], "Label $i")
            data.setCircle()    // false
            data.setLineColor(colors[i], true)

            sets.add(data)
        }

        sets.add(priceData) // price最後
        setLineData(sets)
    }

    private fun setLineData(sets: List<ILineDataSet>) {
        dataSets.value = sets
    }

    fun setLifecycle(lifecycleOwner: LifecycleOwner) {
        dataList.observe(lifecycleOwner) {
            getEntry(STOCK_ID)
        }
    }

    private fun initList() {
        dataSets.value = listOf(LineDataSet(emptyList(), "NONE"))
    }

    private fun getStock() {
        val api = ServiceApi.getter.getStockResult(STOCK_ID)
        api.enqueue(object : Callback<StockData> {
            override fun onResponse(call: Call<StockData>, response: Response<StockData>) {
                if (response.isSuccessful) {
                    val data = response.body()?.dataList
                    dataList.postValue(data)
                }
            }

            override fun onFailure(call: Call<StockData>, t: Throwable) {
                Log.d(DATA_MSG, "Get stock fail: ${t.message}")
            }
        })
    }
}