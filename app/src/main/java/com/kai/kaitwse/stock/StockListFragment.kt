package com.kai.kaitwse.stock

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kai.kaitwse.R
import com.kai.kaitwse.common.applyNavigationBarPadding
import com.kai.kaitwse.common.applyStatusBarPadding
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StockListFragment : Fragment(R.layout.fragment_stock_list) {

    companion object {
        private const val TAG = "StockListFragment"
        private const val MENU_ACTION_ID = 1
    }

    private val viewModel: StockListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.applyStatusBarPadding()
        setupToolbar(view.findViewById(R.id.stock_toolbar))

        view.findViewById<RecyclerView>(R.id.stock_recycler_view).apply {
            applyNavigationBarPadding()
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        observeUiState(view)
    }

    private fun observeUiState(view: View) {
        val loadingView = view.findViewById<ProgressBar>(R.id.stock_loading)
        val recyclerView = view.findViewById<RecyclerView>(R.id.stock_recycler_view)
        val emptyView = view.findViewById<TextView>(R.id.stock_empty_view)
        val errorView = view.findViewById<TextView>(R.id.stock_error_view)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    val showLoading = state.isLoading
                    val showError = !showLoading && state.errorMessage != null
                    val showEmpty = !showLoading && state.errorMessage == null && state.items.isEmpty()
                    val showContent = !showLoading && state.items.isNotEmpty()

                    loadingView.visibility = if (showLoading) View.VISIBLE else View.GONE
                    errorView.visibility = if (showError) View.VISIBLE else View.GONE
                    emptyView.visibility = if (showEmpty) View.VISIBLE else View.GONE
                    recyclerView.visibility = if (showContent) View.VISIBLE else View.GONE

                    errorView.text = state.errorMessage.orEmpty()

                    Log.d(
                        TAG,
                        "uiState: loading=${state.isLoading}, items=${state.items.size}, errorMessage=${state.errorMessage}",
                    )
                }
            }
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
