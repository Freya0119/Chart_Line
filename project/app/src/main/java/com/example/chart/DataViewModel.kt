package com.example.chart

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chart.data.Stock
import com.example.chart.data.StockData
import com.github.mikephil.charting.data.Entry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataViewModel() : ViewModel() {
    // for set
    val stock_id = STOCK_ID

    val dataList = MutableLiveData<List<Stock>?>()

    private val _stock = MutableLiveData<Stock>()
    val stock get() = _stock

    var priceEntry = mutableListOf<Entry>()
    var riverEntry = mutableListOf<List<Entry>>()

    init {
        getStockList()
    }

    private fun updateRiverEntry() {
        val entryList = mutableListOf<List<Entry>>()

        val riverInfoList = stock.value?.riverMapInfo?.sortedBy { it?.yearMonth }
        if (riverInfoList != null) {

            val infoSize = riverInfoList.size ?: 0
            if (infoSize > 0) {
                // x -> index
                val xList = List(infoSize) { it.toFloat() }
                // river line
                val riverList = riverInfoList.map { it?.perStockPriceStandard }
                if (riverList.isNotEmpty()) {

                    val lastInd = riverList[0]?.lastIndex ?: 0
                    if (lastInd > 0) {
                        // high -> low
                        for (i in lastInd downTo 0) {
                            val yList = riverList.map { it?.get(i) ?: 0f }
                            val entry = xList.zip(yList) { x, y -> Entry(x, y) }
                            entryList.add(entry)
                        }
                    }
                }
            }
        }
        riverEntry = entryList
    }

    private fun updatePriceEntry() {
        val riverInfoList = stock.value?.riverMapInfo?.sortedBy { it?.yearMonth }
        if (riverInfoList != null) {

            val infoSize = riverInfoList.size ?: 0
            if (infoSize > 0) {
                // x -> index
                val xList = List(infoSize) { it.toFloat() }

                val priceList = riverInfoList.map { it?.closingPriceMonth ?: 0f }
                val entry = xList.zip(priceList) { x, y -> Entry(x, y) }
                priceEntry = entry.toMutableList()
                // result not true
                Log.d(DATA_MSG, "river first: ${priceEntry[0]}")
            }
        }
    }

    fun updateEntry() {
        updatePriceEntry()
        updateRiverEntry()
    }

    fun setLifecycle(lifecycleOwner: LifecycleOwner) {
        dataList.observe(lifecycleOwner) {
            getStockSingle(stock_id)
            updateEntry()
        }
    }

    private fun getStockSingle(code: Int) {
        val s = dataList.value?.find { it.stockCode == code }
        s?.let {
            _stock.value = it
        }
    }

    private fun getStockList() {
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