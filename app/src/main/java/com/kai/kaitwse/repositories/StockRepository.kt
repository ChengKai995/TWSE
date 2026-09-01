package com.kai.kaitwse.repositories

import android.util.Log
import com.kai.kaitwse.dto.bwibbu_all.BwibbuAllDto
import com.kai.kaitwse.dto.stock_day_all.StockDayAllDto
import com.kai.kaitwse.dto.stock_day_avg_all.StockDayAvgAllDto
import com.kai.kaitwse.network.RetrofitClient
import com.kai.kaitwse.network.StockService

private val TAG = "StockRepository"
class StockRepository(
    private val stockService: StockService = RetrofitClient.stockService
) : StockDataSource {
    override
    suspend fun getStockDayAll(): Result<StockDayAllDto> = runCatching {
        Log.d(TAG, "StockDayAll_Request")
        val response = stockService.getStockDayAll()
        Log.d(TAG, "getStockDayAll_Response: ${response.size}")
        response
    }

    override
    suspend fun getBwibbuAll(): Result<BwibbuAllDto> = runCatching {
        Log.d(TAG, "BwibbuAll_Request")
        val response = stockService.getBwibbuAll()
        Log.d(TAG, "BwibbuAll_Response: ${response.size}")
        response
    }

    override
    suspend fun getStockDayAvgAll(): Result<StockDayAvgAllDto> = runCatching {
        Log.d(TAG, "StockDayAvgAll_Request")
        val response = stockService.getStockDayAvgAll()
        Log.d(TAG, "StockDayAvgAll_Response: ${response.size}")
        response
    }
}
