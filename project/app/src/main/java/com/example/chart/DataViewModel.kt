package com.example.chart

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chart.data.Stock
import com.example.chart.data.StockData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
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

    fun updateDataSets(entryList: List<Entry>) {
        val sets = mutableListOf<ILineDataSet>()
        sets.add(LineDataSet(entryList, "LabelA"))
        dataSets.value = sets
//        Log.d(
//            DATA_MSG, "entry: ${entry.value?.first()}, " +
//                    "sets: ${sets.first()?.entryCount}, " +
//                    "datasets: ${dataSets.value?.first()?.entryCount}"
//        )
    }

    fun setLifecycle(lifecycleOwner: LifecycleOwner) {
        dataList.observe(lifecycleOwner) {
            Log.d(DATA_MSG, "Observe")
            getEntryList(STOCK_ID)  // 好多list
        }
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

    private fun getEntryList(stockId: Int) {
        val riverMap = dataList.value?.find { it.stockCode == stockId }?.riverMapInfo
        if (riverMap != null) {
            if (riverMap.isNotEmpty()) {
                Log.d(DATA_MSG, "Began entry")
//                val date = riverMap.map { it?.yearMonth?.div(100) ?: 0 }  // data -> x軸
                val price = riverMap.map { it?.closingPriceMonth ?: 0f }    // price -> y軸  // 倒過來，軸依賴
                val lowestPer = riverMap.map { it?.perStockPriceStandard?.get(0) ?: 0f }  // lowest基準
                val lowerPer = riverMap.map { it?.perStockPriceStandard?.get(1) ?: 0f }  // lowest基準
                val mediumLowerPer = riverMap.map { it?.perStockPriceStandard?.get(2) ?: 0f }  // lowest基準
                val mediumHigherPer = riverMap.map { it?.perStockPriceStandard?.get(3) ?: 0f }  // lowest基準
                val higherPer = riverMap.map { it?.perStockPriceStandard?.get(4) ?: 0f }  // lowest基準
                val highestPer = riverMap.map { it?.perStockPriceStandard?.get(5) ?: 0f }  // lowest基準

                val indexArr = MutableList(price.size) {
                    it.toFloat()
                }
                val entryList = indexArr.zip(lowerPer) { x, y -> Entry(x.toFloat(), y) }
//                entry.value = entryList
//                Log.d(DATA_MSG, "finish entry: ${entryList?.size}")

                updateDataSets(entryList)
            }
        }
    }
}