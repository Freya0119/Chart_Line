package com.example.chart

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chart.data.Stock
import com.example.chart.data.StockData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataViewModel() : ViewModel() {
    val dataList = MutableLiveData<List<Stock>?>(emptyList())

    val entry = MutableLiveData<List<Entry>?>(emptyList())  // ?LiveData

    val dataSets = MutableLiveData<List<ILineDataSet>>()

    init {
        initList()
        getStock()
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

    private fun getEntry(stockId: Int) {
        val riverMap = dataList.value?.find { it.stockCode == stockId }?.riverMapInfo
        if (riverMap != null) {
            val entryLists = mutableListOf<List<Entry>>()
            //                entry.value = entryList
            //                Log.d(DATA_MSG, "finish entry: ${entryList?.size}")
            //                Log.d(DATA_MSG, "Began entry")
            //                val date = riverMap.map { it?.yearMonth?.div(100) ?: 0 }  // data -> x軸

            val price = riverMap.map { it?.closingPriceMonth ?: 0f }    // price -> y軸  // 倒過來，軸依賴

            val indexArr = MutableList(price.size) {
                it.toFloat()
            }
            val priceEntryList = indexArr.zip(price) { x, y -> Entry(x.toFloat(), y) }
            entryLists.add(priceEntryList)

            val perSize = 6 - 1 // 河流圖
            for (i in 0..perSize) {
                val perList = riverMap.map { it?.perStockPriceStandard?.get(i) ?: 0f }  // lowest基準
                val entryList = indexArr.zip(perList) { x, y -> Entry(x, y) }
                entryLists.add(entryList)
            }

            Log.d(DATA_MSG, "Entry list size: ${entryLists.size}")
            updateDataSets(entryLists)
        }
    }

    private fun updateDataSets(entryList: List<List<Entry>>) {
        val sets = mutableListOf<ILineDataSet>()
        var count = 0
        for (list in entryList) {
            count++
            sets.add(LineDataSet(list, "Label $count}"))
        }
        setLineData(sets)
    }

    private fun setLineData(sets: List<ILineDataSet>) {
        dataSets.value = sets
    }

    fun setLifecycle(lifecycleOwner: LifecycleOwner) {
        dataList.observe(lifecycleOwner) {
            getEntry(STOCK_ID)  // 好多list
        }
    }
}