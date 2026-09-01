package com.kai.kaitwse.stock

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.kai.kaitwse.R
import com.kai.kaitwse.common.applyNavigationBarPadding
import com.kai.kaitwse.common.applyStatusBarPadding
import com.kai.kaitwse.databinding.FragmentStockListBinding
import com.google.android.material.appbar.MaterialToolbar
import com.kai.kaitwse.stock.compose.view.StockInfoDialog
import com.kai.kaitwse.stock.compose.view.StockSortSheet
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StockListFragment : Fragment(R.layout.fragment_stock_list) {

    companion object {
        private const val TAG = "StockListFragment"
        private const val MENU_ACTION_ID = 1
        private const val FADE_DURATION = 500L
    }

    private var _binding: FragmentStockListBinding? = null
    private val binding: FragmentStockListBinding
        get() = _binding!!

    private val viewModel: StockListViewModel by viewModels()
    private val stockListAdapter = StockListAdapter { item ->
        viewModel.showStockInfo(item)
    }
    private var shouldScrollToTopAfterSort = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStockListBinding.bind(view)

        binding.root.applyStatusBarPadding()
        setupToolbar(binding.stockToolbar)
        setupStockComposeView()

        binding.stockRecyclerView.apply {
            applyNavigationBarPadding()
            adapter = stockListAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        observeUiState()
    }

    private fun setupToolbar(toolbar: MaterialToolbar) {
        toolbar.menu.clear()
        toolbar.menu.add(
            Menu.NONE,
            MENU_ACTION_ID,
            Menu.NONE,
            getString(R.string.stock_menu_title),
        ).apply {
            setIcon(android.R.drawable.ic_menu_sort_by_size)
            setContentDescription(getString(R.string.stock_menu_content_description))
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                MENU_ACTION_ID -> {
                    showSortBottomSheet()
                    true
                }

                else -> false
            }
        }
    }

    private fun setupStockComposeView() {
        binding.stockComposeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    val uiState by viewModel.uiState.collectAsState()

                    StockSortSheet(
                        visible = uiState.isSortSheetVisible,
                        onDismissRequest = viewModel::hideSortSheet,
                        onAscendingClick = { sortByCode(StockSortOrder.ASCENDING) },
                        onDescendingClick = { sortByCode(StockSortOrder.DESCENDING) },
                    )

                    uiState.selectedStockInfo?.let { item ->
                        StockInfoDialog(
                            item = item,
                            onDismiss = viewModel::hideStockInfo,
                        )
                    }
                }
            }
        }
    }

    private fun showSortBottomSheet() {
        viewModel.showSortSheet()
    }

    private fun sortByCode(sortOrder: StockSortOrder) {
        viewModel.hideSortSheet()
        shouldScrollToTopAfterSort = true
        if (sortOrder == StockSortOrder.ASCENDING) {
            viewModel.sortByCodeAscending()
            return
        }

        viewModel.sortByCodeDescending()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    renderList(state.items)
                    renderContentState(state)
                }
            }
        }
    }

    private fun renderList(items: List<StockListItemUiState>) {
        stockListAdapter.submitList(items) {
            if (shouldScrollToTopAfterSort) {
                binding.stockRecyclerView.stopScroll()
                (binding.stockRecyclerView.layoutManager as? LinearLayoutManager)
                    ?.scrollToPositionWithOffset(0, 0)
                shouldScrollToTopAfterSort = false
            }
        }
    }

    private fun renderContentState(state: StockListUiState) {
        val showLoading = state.isLoading
        val showError = !showLoading && state.errorMessage != null
        val showEmpty = !showLoading && state.errorMessage == null && state.items.isEmpty()
        val showContent = !showLoading && state.items.isNotEmpty()

        binding.stockErrorView.text = state.errorMessage.orEmpty()
        binding.stockLoading.updateVisibilityAnimated(showLoading)
        binding.stockErrorView.updateVisibilityAnimated(showError)
        binding.stockEmptyView.updateVisibilityAnimated(showEmpty)
        binding.stockRecyclerView.updateVisibilityAnimated(showContent)

        if (showError) {
            Log.e(TAG, "load stock list failed: ${state.errorMessage}")
        }
    }

    override fun onDestroyView() {
        binding.stockRecyclerView.adapter = null
        _binding = null
        super.onDestroyView()
    }

    private fun View.updateVisibilityAnimated(shouldShow: Boolean) {
        if (shouldShow == isVisible) return

        animate().cancel()
        if (shouldShow) {
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(FADE_DURATION)
                .start()
        } else {
            animate()
                .alpha(0f)
                .setDuration(FADE_DURATION)
                .withEndAction {
                    visibility = View.GONE
                }
                .start()
        }
    }
}
