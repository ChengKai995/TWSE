package com.kai.kaitwse.stock

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.core.view.isVisible

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
    private val stockListAdapter = StockListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStockListBinding.bind(view)

        binding.root.applyStatusBarPadding()
        setupToolbar(binding.stockToolbar)

        binding.stockRecyclerView.apply {
            applyNavigationBarPadding()
            adapter = stockListAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        observeUiState()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    val showLoading = state.isLoading
                    val showError = !showLoading && state.errorMessage != null
                    val showEmpty = !showLoading && state.errorMessage == null && state.items.isEmpty()
                    val showContent = !showLoading && state.items.isNotEmpty()

                    stockListAdapter.submitList(state.items)
                    binding.stockErrorView.text = state.errorMessage.orEmpty()
                    binding.stockLoading.updateVisibilityAnimated(showLoading)
                    binding.stockErrorView.updateVisibilityAnimated(showError)
                    binding.stockEmptyView.updateVisibilityAnimated(showEmpty)
                    binding.stockRecyclerView.updateVisibilityAnimated(showContent)
                    if (showError) {
                        Log.e(TAG, "Failed to show stock list: ${state.errorMessage}")
                    }
                }
            }
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

    private fun setupToolbar(toolbar: MaterialToolbar) {
        toolbar.menu.clear()
        toolbar.menu.add(
            Menu.NONE,
            MENU_ACTION_ID,
            Menu.NONE,
            getString(R.string.stock_menu_title),
        ).apply {
            setIcon(android.R.drawable.ic_menu_sort_by_size)
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                MENU_ACTION_ID -> {
                    Log.d(TAG, "Toolbar menu clicked")
                    true
                }

                else -> false
            }
        }
    }
}
