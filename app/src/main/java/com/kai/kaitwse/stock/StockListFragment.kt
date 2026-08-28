package com.kai.kaitwse.stock

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kai.kaitwse.R
import com.kai.kaitwse.common.applyNavigationBarPadding
import com.kai.kaitwse.common.applyStatusBarPadding
import com.google.android.material.appbar.MaterialToolbar

class StockListFragment : Fragment(R.layout.fragment_stock_list) {

    companion object {
        private const val TAG = "StockListFragment"
        private const val MENU_ACTION_ID = 1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.applyStatusBarPadding()
        setupToolbar(view.findViewById(R.id.stock_toolbar))

        view.findViewById<RecyclerView>(R.id.stock_recycler_view).apply {
            applyNavigationBarPadding()
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
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
