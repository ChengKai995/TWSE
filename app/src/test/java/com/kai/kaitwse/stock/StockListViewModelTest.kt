package com.kai.kaitwse.stock

import com.kai.kaitwse.MainDispatcherRule
import com.kai.kaitwse.dto.bwibbu_all.BwibbuAllDto
import com.kai.kaitwse.dto.bwibbu_all.BwibbuAllItem
import com.kai.kaitwse.dto.stock_day_all.StockDayAllDto
import com.kai.kaitwse.dto.stock_day_all.StockDayAllDtoItem
import com.kai.kaitwse.dto.stock_day_avg_all.StockDayAvgAllDto
import com.kai.kaitwse.dto.stock_day_avg_all.StockDayAvgAllDtoItem
import com.kai.kaitwse.repositories.StockDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StockListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `init loads stock list and keeps default sort order`() = runTest {
        val viewModel = StockListViewModel(
            stockRepository = FakeStockDataSource(),
        )

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals(2, state.items.size)
        assertEquals(StockSortOrder.NONE, state.currentSortOrder)
        assertEquals("2330", state.items[0].code)
        assertEquals("1101", state.items[1].code)
    }

    @Test
    fun `sortByCodeDescending updates item order and sort state`() = runTest {
        val viewModel = StockListViewModel(
            stockRepository = FakeStockDataSource(),
        )

        advanceUntilIdle()
        viewModel.sortByCodeDescending()

        val state = viewModel.uiState.value
        assertEquals(StockSortOrder.DESCENDING, state.currentSortOrder)
        assertEquals(listOf("2330", "1101"), state.items.map { it.code })
    }

    @Test
    fun `init shows error when any request fails`() = runTest {
        val viewModel = StockListViewModel(
            stockRepository = FakeStockDataSource(
                stockDayAllResult = Result.failure(IllegalStateException("boom")),
            ),
        )

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("股票資料載入失敗", state.errorMessage)
        assertTrue(state.items.isEmpty())
    }

    private class FakeStockDataSource(
        private val stockDayAllResult: Result<StockDayAllDto> = Result.success(
            StockDayAllDto().apply {
                add(
                    StockDayAllDtoItem(
                        change = "1.5000",
                        closingPrice = "601.00",
                        code = "2330",
                        date = "1150826",
                        highestPrice = "605.00",
                        lowestPrice = "598.00",
                        name = "台積電",
                        openingPrice = "600.00",
                        tradeValue = "3600000000",
                        tradeVolume = "6000000",
                        transaction = "1000",
                    ),
                )
                add(
                    StockDayAllDtoItem(
                        change = "-0.5000",
                        closingPrice = "45.30",
                        code = "1101",
                        date = "1150826",
                        highestPrice = "45.80",
                        lowestPrice = "45.10",
                        name = "台泥",
                        openingPrice = "45.70",
                        tradeValue = "180000000",
                        tradeVolume = "2000000",
                        transaction = "800",
                    ),
                )
            },
        ),
        private val bwibbuAllResult: Result<BwibbuAllDto> = Result.success(
            BwibbuAllDto().apply {
                add(
                    BwibbuAllItem(
                        code = "2330",
                        date = "1150826",
                        dividendYield = "1.20",
                        name = "台積電",
                        pbRatio = "5.80",
                        peRatio = "18.20",
                    ),
                )
                add(
                    BwibbuAllItem(
                        code = "1101",
                        date = "1150826",
                        dividendYield = "2.10",
                        name = "台泥",
                        pbRatio = "1.30",
                        peRatio = "12.50",
                    ),
                )
            },
        ),
        private val stockDayAvgAllResult: Result<StockDayAvgAllDto> = Result.success(
            StockDayAvgAllDto().apply {
                add(
                    StockDayAvgAllDtoItem(
                        closingPrice = "601.00",
                        code = "2330",
                        date = "1150826",
                        monthlyAveragePrice = "590.00",
                        name = "台積電",
                    ),
                )
                add(
                    StockDayAvgAllDtoItem(
                        closingPrice = "45.30",
                        code = "1101",
                        date = "1150826",
                        monthlyAveragePrice = "46.00",
                        name = "台泥",
                    ),
                )
            },
        ),
    ) : StockDataSource {
        override suspend fun getStockDayAll(): Result<StockDayAllDto> = stockDayAllResult

        override suspend fun getBwibbuAll(): Result<BwibbuAllDto> = bwibbuAllResult

        override suspend fun getStockDayAvgAll(): Result<StockDayAvgAllDto> = stockDayAvgAllResult
    }
}
