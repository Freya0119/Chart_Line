package com.example.chart

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.chart.data.Stock
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(stock: Stock)

    @Update
    suspend fun update(stock: Stock)

    @Delete
    suspend fun delete(stock: Stock)

    @Query("SELECT * from single_stock WHERE stockCode = stockCode")
    fun getStock(stockCode: Int): Flow<Stock>
}