package com.kai.kaitwse.stock

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kai.kaitwse.R
import com.kai.kaitwse.databinding.ItemStockBinding

class StockListAdapter(
    private val onItemClick: (StockListItemUiState) -> Unit,
) : ListAdapter<StockListItemUiState, StockListAdapter.StockViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val binding = ItemStockBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return StockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }

    class StockViewHolder(
        private val binding: ItemStockBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: StockListItemUiState,
            onItemClick: (StockListItemUiState) -> Unit,
        ) {
            bindTextView(binding.textStockCode, item.code)
            bindTextView(binding.textStockName, item.name)
            bindTextView(binding.textOpeningPrice, item.openingPrice)
            bindTextView(binding.textClosingPrice, item.closingPrice, item.closingPriceTrend)
            bindTextView(binding.textHighestPrice, item.highestPrice)
            bindTextView(binding.textLowestPrice, item.lowestPrice)
            bindTextView(binding.textChange, item.change, item.changeTrend)
            bindTextView(binding.textMonthlyAveragePrice, item.monthlyAveragePrice)
            bindTextView(binding.textTransaction, item.transaction)
            bindTextView(binding.textTradeVolume, item.tradeVolume)
            bindTextView(binding.textTradeValue, item.tradeValue)
            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }

        private fun bindTextView(
            textView: TextView,
            rawText: String,
            trend: PriceTrend? = null,
        ) {
            if (rawText.isBlank()) {
                textView.text = " - "
                textView.setTextColor(getColor(itemView.context, R.color.app_on_surface))
                return
            }

            val colorRes = when (trend) {
                PriceTrend.UP -> R.color.stock_positive
                PriceTrend.DOWN -> R.color.stock_negative
                PriceTrend.FLAT,
                PriceTrend.UNKNOWN,
                null,
                -> R.color.app_on_surface
            }

            textView.text = rawText
            textView.setTextColor(getColor(itemView.context, colorRes))
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<StockListItemUiState>() {
        override fun areItemsTheSame(
            oldItem: StockListItemUiState,
            newItem: StockListItemUiState,
        ): Boolean = oldItem.code == newItem.code

        override fun areContentsTheSame(
            oldItem: StockListItemUiState,
            newItem: StockListItemUiState,
        ): Boolean = oldItem == newItem
    }
}
