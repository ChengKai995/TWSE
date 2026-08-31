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

class StockListFragment : Fragment(R.layout.fragment_stock_list) {

    companion object {
        private const val TAG = "StockListFragment"
        private const val MENU_ACTION_ID = 1
    }

    private var _binding: FragmentStockListBinding? = null
    private val binding: FragmentStockListBinding
        get() = _binding!!

    private val viewModel: StockListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStockListBinding.bind(view)

        binding.root.applyStatusBarPadding()
        setupToolbar(binding.stockToolbar)

        binding.stockRecyclerView.apply {
            applyNavigationBarPadding()
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

                    binding.stockLoading.visibility = if (showLoading) View.VISIBLE else View.GONE
                    binding.stockErrorView.visibility = if (showError) View.VISIBLE else View.GONE
                    binding.stockEmptyView.visibility = if (showEmpty) View.VISIBLE else View.GONE
                    binding.stockRecyclerView.visibility = if (showContent) View.VISIBLE else View.GONE

                    binding.stockErrorView.text = state.errorMessage.orEmpty()

                    Log.d(
                        TAG,
                        "uiState: loading=${state.isLoading}, items=${state.items.size}, errorMessage=${state.errorMessage}",
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
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
