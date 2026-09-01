package com.kai.kaitwse.repositories

import com.kai.kaitwse.dto.bwibbu_all.BwibbuAllDto
import com.kai.kaitwse.dto.stock_day_all.StockDayAllDto
import com.kai.kaitwse.dto.stock_day_avg_all.StockDayAvgAllDto

interface StockDataSource {
    suspend fun getStockDayAll(): Result<StockDayAllDto>
    suspend fun getBwibbuAll(): Result<BwibbuAllDto>
    suspend fun getStockDayAvgAll(): Result<StockDayAvgAllDto>
}
