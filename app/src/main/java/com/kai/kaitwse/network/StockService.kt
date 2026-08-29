package com.kai.kaitwse.network

import com.kai.kaitwse.dto.bwibbu_all.BwibbuAllDto
import com.kai.kaitwse.dto.stock_day_all.StockDayAllDto
import com.kai.kaitwse.dto.stock_day_avg_all.StockDayAvgAllDto
import retrofit2.http.GET

interface StockService {
    @GET("v1/exchangeReport/STOCK_DAY_ALL")
    suspend fun getStockDayAll(): StockDayAllDto

    @GET("v1/exchangeReport/BWIBBU_ALL")
    suspend fun getBwibbuAll(): BwibbuAllDto

    @GET("v1/exchangeReport/STOCK_DAY_AVG_ALL")
    suspend fun getStockDayAvgAll(): StockDayAvgAllDto

}