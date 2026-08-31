package com.kai.kaitwse.stock

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.kai.kaitwse.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.R as MaterialR
import androidx.core.graphics.drawable.toDrawable

class StockSortBottomSheet : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "StockSortBottomSheet"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    Surface {
                        StockSortSheetContent(
                            onAscendingClick = {
                                setSortResult(ArgKeys.STOCK_SORT_ORDER_ASCENDING)
                            },
                            onDescendingClick = {
                                setSortResult(ArgKeys.STOCK_SORT_ORDER_DESCENDING)
                            },
                        )
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        (dialog as? BottomSheetDialog)
            ?.findViewById<FrameLayout>(MaterialR.id.design_bottom_sheet)
            ?.apply {
                setBackgroundResource(R.drawable.bg_stock_sort_bottom_sheet)
                clipToOutline = true
            }
    }

    private fun setSortResult(sortOrder: String) {
        parentFragmentManager.setFragmentResult(
            ArgKeys.STOCK_SORT_RESULT_KEY,
            Bundle().apply {
                putString(ArgKeys.STOCK_SORT_ORDER_KEY, sortOrder)
            },
        )
        dismiss()
    }
}

@Composable
private fun StockSortSheetContent(
    onAscendingClick: () -> Unit,
    onDescendingClick: () -> Unit,
) {
    val context = LocalContext.current
    val sheetBackground = ComposeColor(ContextCompat.getColor(context, com.kai.kaitwse.R.color.stock_item_background))
    val handleColor = ComposeColor(ContextCompat.getColor(context, com.kai.kaitwse.R.color.app_toolbar_divider))

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = sheetBackground,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            DragHandle(
                color = handleColor,
                width = 44.dp,
                height = 5.dp,
            )
            SortOption(
                text = "依股票代號升序",
                onClick = onAscendingClick,
            )
            SortOption(
                text = "依股票代號降序",
                onClick = onDescendingClick,
            )
        }
    }
}

@Composable
private fun DragHandle(
    color: ComposeColor,
    width: Dp,
    height: Dp,
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(width = width, height = height)
                .clip(RoundedCornerShape(percent = 50))
                .background(color),
        )
    }
}

@Composable
private fun SortOption(
    text: String,
    onClick: () -> Unit,
) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onSurface,
    )
}
