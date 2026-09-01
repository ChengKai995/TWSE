package com.kai.kaitwse.stock

import com.kai.kaitwse.dto.bwibbu_all.BwibbuAllItem
import com.kai.kaitwse.dto.stock_day_all.StockDayAllDtoItem
import com.kai.kaitwse.dto.stock_day_avg_all.StockDayAvgAllDtoItem
import org.junit.Assert.assertEquals
import org.junit.Test

class StockListItemUiStateMapperTest {

    @Test
    fun `toStockListItemUiState maps values and formats numbers`() {
        val stockDayItem = StockDayAllDtoItem(
            change = "0.3300",
            closingPrice = "14.71",
            code = "00400A",
            date = "1150826",
            highestPrice = "14.73",
            lowestPrice = "14.33",
            name = "主動國泰動能高息",
            openingPrice = "14.34",
            tradeValue = "420203228",
            tradeVolume = "28771914",
            transaction = "5727",
        )
        val bwibbuItem = BwibbuAllItem(
            code = "00400A",
            date = "1150826",
            dividendYield = "1.88",
            name = "主動國泰動能高息",
            pbRatio = "0.96",
            peRatio = "25.81",
        )
        val stockDayAvgItem = StockDayAvgAllDtoItem(
            closingPrice = "14.71",
            code = "00400A",
            date = "1150826",
            monthlyAveragePrice = "14.36",
            name = "主動國泰動能高息",
        )

        val uiState = stockDayItem.toStockListItemUiState(
            bwibbuItem = bwibbuItem,
            stockDayAvgItem = stockDayAvgItem,
        )

        assertEquals("00400A", uiState.code)
        assertEquals("主動國泰動能高息", uiState.name)
        assertEquals("0.33", uiState.change)
        assertEquals("420.20M", uiState.tradeValue)
        assertEquals("25.81", uiState.peRatio)
        assertEquals("1.88", uiState.dividendYield)
        assertEquals("0.96", uiState.pbRatio)
        assertEquals(PriceTrend.UP, uiState.changeTrend)
        assertEquals(PriceTrend.UP, uiState.closingPriceTrend)
    }

    @Test
    fun `toStockListItemUiState falls back safely when source values are null or invalid`() {
        val stockDayItem = StockDayAllDtoItem(
            change = null,
            closingPrice = "abc",
            code = null,
            date = null,
            highestPrice = null,
            lowestPrice = null,
            name = null,
            openingPrice = null,
            tradeValue = null,
            tradeVolume = null,
            transaction = null,
        )

        val uiState = stockDayItem.toStockListItemUiState(
            bwibbuItem = null,
            stockDayAvgItem = null,
        )

        assertEquals("", uiState.code)
        assertEquals("", uiState.name)
        assertEquals("", uiState.openingPrice)
        assertEquals("", uiState.tradeValue)
        assertEquals("", uiState.peRatio)
        assertEquals(PriceTrend.UNKNOWN, uiState.changeTrend)
        assertEquals(PriceTrend.UNKNOWN, uiState.closingPriceTrend)
    }
}
