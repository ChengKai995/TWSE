package com.kai.kaitwse.stock.compose.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kai.kaitwse.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockSortSheet(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    onAscendingClick: () -> Unit,
    onDescendingClick: () -> Unit,
) {
    if (!visible) return

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = colorResource(id = R.color.app_background),
    ) {
        StockSortSheetContent(
            onAscendingClick = onAscendingClick,
            onDescendingClick = onDescendingClick,
        )
    }
}

@Composable
private fun StockSortSheetContent(
    onAscendingClick: () -> Unit,
    onDescendingClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        SortOption(
            text = stringResource(id = R.string.stock_sort_code_ascending),
            onClick = onAscendingClick,
        )
        SortOption(
            text = stringResource(id = R.string.stock_sort_code_descending),
            onClick = onDescendingClick,
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
        color = colorResource(id = R.color.app_on_surface),
    )
}


@Preview(showBackground = true)
@Composable
fun StockSortSheetPreview() {
    Surface {
        StockSortSheetContent(
        onAscendingClick = {},
        onDescendingClick = {},
        )
    }
}
