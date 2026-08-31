package com.kai.kaitwse.stock

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kai.kaitwse.repositories.StockRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StockListViewModel(
    private val stockRepository: StockRepository = StockRepository()
) : ViewModel() {

    companion object {
        private const val TAG = "StockListViewModel"
    }

    private val _uiState = MutableStateFlow(StockListUiState())
    val uiState: StateFlow<StockListUiState> = _uiState.asStateFlow()
    private var originalItems: List<StockListItemUiState> = emptyList()

    init {
        fetchInitialData()
    }

    private fun fetchInitialData() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                )
            }
            Log.d(TAG, "fetchInitialData: start")

            val stockDayAllDeferred = async { stockRepository.getStockDayAll() }
            val bwibbuAllDeferred = async { stockRepository.getBwibbuAll() }
            val stockDayAvgAllDeferred = async { stockRepository.getStockDayAvgAll() }

            val stockDayAllResult = stockDayAllDeferred.await()
            val bwibbuAllResult = bwibbuAllDeferred.await()
            val stockDayAvgAllResult = stockDayAvgAllDeferred.await()

            if (
                stockDayAllResult.isFailure ||
                bwibbuAllResult.isFailure ||
                stockDayAvgAllResult.isFailure
            ) {
                stockDayAllResult.exceptionOrNull()?.let {
                    Log.e(TAG, "STOCK_DAY_ALL failed", it)
                }
                bwibbuAllResult.exceptionOrNull()?.let {
                    Log.e(TAG, "BWIBBU_ALL failed", it)
                }
                stockDayAvgAllResult.exceptionOrNull()?.let {
                    Log.e(TAG, "STOCK_DAY_AVG_ALL failed", it)
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        items = emptyList(),
                        errorMessage = "股票資料載入失敗",
                    )
                }
                return@launch
            }

            val stockDayAll = stockDayAllResult.getOrThrow()
            val bwibbuAll = bwibbuAllResult.getOrThrow()
            val stockDayAvgAll = stockDayAvgAllResult.getOrThrow()
            Log.d(
                TAG,
                "fetchInitialData success: stockDayAll=${stockDayAll.size}, bwibbuAll=${bwibbuAll.size}, stockDayAvgAll=${stockDayAvgAll.size}",
            )

            val stockDayAvgAllByCode = stockDayAvgAll
                .filter { !it.code.isNullOrBlank() }
                .associateBy { it.code.orEmpty() }
            val items = stockDayAll.map { stockDayItem ->
                stockDayItem.toStockListItemUiState(
                    stockDayAvgItem = stockDayAvgAllByCode[stockDayItem.code.orEmpty()],
                )
            }
            originalItems = items

            _uiState.update {
                it.copy(
                    isLoading = false,
                    items = applySort(items, it.currentSortOrder),
                    errorMessage = null,
                )
            }
        }
    }

    fun sortByCodeAscending() {
        updateSortedItems(StockSortOrder.ASCENDING)
    }

    fun sortByCodeDescending() {
        updateSortedItems(StockSortOrder.DESCENDING)
    }

    private fun updateSortedItems(sortOrder: StockSortOrder) {
        _uiState.update {
            it.copy(
                items = applySort(originalItems, sortOrder),
                currentSortOrder = sortOrder,
            )
        }
    }

    private fun applySort(
        items: List<StockListItemUiState>,
        sortOrder: StockSortOrder,
    ): List<StockListItemUiState> {
        return when (sortOrder) {
            StockSortOrder.NONE -> items
            StockSortOrder.ASCENDING -> items.sortedBy { it.code }
            StockSortOrder.DESCENDING -> items.sortedByDescending { it.code }
        }
    }
}

data class StockListUiState(
    val isLoading: Boolean = false,
    val items: List<StockListItemUiState> = emptyList(),
    val errorMessage: String? = null,
    val currentSortOrder: StockSortOrder = StockSortOrder.NONE,
)

data class StockListItemUiState(
    val code: String,
    val name: String,
    val openingPrice: String,
    val closingPrice: String,
    val highestPrice: String,
    val lowestPrice: String,
    val change: String,
    val monthlyAveragePrice: String,
    val transaction: String,
    val tradeVolume: String,
    val tradeValue: String,
    val closingPriceTrend: PriceTrend,
    val changeTrend: PriceTrend,
)

enum class StockSortOrder {
    NONE,
    ASCENDING,
    DESCENDING,
}
