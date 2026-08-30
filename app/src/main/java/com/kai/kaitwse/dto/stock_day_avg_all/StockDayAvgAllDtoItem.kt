package com.kai.kaitwse.dto.stock_day_avg_all

import com.google.gson.annotations.SerializedName

data class StockDayAvgAllDtoItem(
    @SerializedName("ClosingPrice")
    val closingPrice: String?,
    @SerializedName("Code")
    val code: String?,
    @SerializedName("Date")
    val date: String?,
    @SerializedName("MonthlyAveragePrice")
    val monthlyAveragePrice: String?,
    @SerializedName("Name")
    val name: String?
)
