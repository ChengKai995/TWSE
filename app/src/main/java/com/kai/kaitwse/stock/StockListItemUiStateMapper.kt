package com.kai.kaitwse.stock

import com.kai.kaitwse.dto.stock_day_all.StockDayAllDtoItem
import com.kai.kaitwse.dto.stock_day_avg_all.StockDayAvgAllDtoItem

enum class PriceTrend {
    UP,
    DOWN,
    FLAT,
    UNKNOWN,
}

fun StockDayAllDtoItem.toStockListItemUiState(
    stockDayAvgItem: StockDayAvgAllDtoItem?,
): StockListItemUiState {
    val stockCode = code.orEmpty()
    val closingPriceValue = closingPrice.orEmpty()
    val monthlyAveragePriceValue = stockDayAvgItem?.monthlyAveragePrice.orEmpty()
    val changeValue = change.orEmpty()

    return StockListItemUiState(
        code = stockCode,
        name = name.orEmpty(),
        openingPrice = openingPrice.orEmpty(),
        closingPrice = closingPriceValue,
        highestPrice = highestPrice.orEmpty(),
        lowestPrice = lowestPrice.orEmpty(),
        change = changeValue,
        monthlyAveragePrice = monthlyAveragePriceValue,
        transaction = transaction.orEmpty(),
        tradeVolume = tradeVolume.orEmpty(),
        tradeValue = tradeValue.orEmpty(),
        closingPriceTrend = getClosingPriceTrend(
            closingPrice = closingPriceValue,
            monthlyAveragePrice = monthlyAveragePriceValue,
        ),
        changeTrend = getChangeTrend(changeValue),
    )
}

private fun getChangeTrend(change: String): PriceTrend {
    val value = change.toDoubleOrNull() ?: return PriceTrend.UNKNOWN
    return if (value > 0) {
        PriceTrend.UP
    } else if (value < 0) {
        PriceTrend.DOWN
    } else {
        PriceTrend.FLAT
    }
}

private fun getClosingPriceTrend(
    closingPrice: String,
    monthlyAveragePrice: String,
): PriceTrend {
    val closingValue = closingPrice.toDoubleOrNull() ?: return PriceTrend.UNKNOWN
    val monthlyAverageValue = monthlyAveragePrice.toDoubleOrNull() ?: return PriceTrend.UNKNOWN

    return if (closingValue > monthlyAverageValue) {
        PriceTrend.UP
    } else if (closingValue < monthlyAverageValue) {
        PriceTrend.DOWN
    } else {
        PriceTrend.FLAT
    }
}
