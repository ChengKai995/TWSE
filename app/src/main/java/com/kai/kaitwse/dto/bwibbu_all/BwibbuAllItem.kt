package com.kai.kaitwse.dto.bwibbu_all

import com.google.gson.annotations.SerializedName

data class BwibbuAllItem(
    @SerializedName("Code")
    val code: String,
    @SerializedName("Date")
    val date: String,
    @SerializedName("DividendYield")
    val dividendYield: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("PBratio")
    val pbRatio: String,
    @SerializedName("PEratio")
    val peRatio: String
)
