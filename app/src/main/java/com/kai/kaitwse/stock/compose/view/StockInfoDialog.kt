package com.kai.kaitwse.stock.compose.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kai.kaitwse.R
import com.kai.kaitwse.stock.StockListItemUiState

@Composable
fun StockInfoDialog(
    item: StockListItemUiState,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = colorResource(id = R.color.stock_item_background),
        ) {
            StockInfoDialogContent(
                code = item.code,
                name = item.name,
                peRatio = item.peRatio,
                dividendYield = item.dividendYield,
                pbRatio = item.pbRatio,
                onDismiss = onDismiss,
            )
        }
    }
}

@Composable
fun StockInfoDialogContent(
    code: String,
    name: String,
    peRatio: String,
    dividendYield: String,
    pbRatio: String,
    onDismiss: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
    ) {
        Text(
            text = code,
            style = TextStyle(
                color = colorResource(id = R.color.app_on_surface),
                fontSize = 14.sp,
                fontWeight = FontWeight.W400,
            ),
        )
        Text(
            text = name,
            style = TextStyle(
                color = colorResource(id = R.color.app_on_surface),
                fontSize = 24.sp,
                fontWeight = FontWeight.W500,
            ),
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp),
        )
        StockInfoRow(
            label = stringResource(id = R.string.stock_info_pe_ratio),
            value = peRatio,
        )
        StockInfoRow(
            label = stringResource(id = R.string.stock_info_dividend_yield),
            value = dividendYield,
        )
        StockInfoRow(
            label = stringResource(id = R.string.stock_info_pb_ratio),
            value = pbRatio,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(id = R.string.common_close),
                    style = TextStyle(
                        color = colorResource(id = R.color.app_on_surface),
                        fontSize = 16.sp,
                    ),
                )
            }
        }
    }
}

@Composable
private fun StockInfoRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = TextStyle(
                color = colorResource(id = R.color.app_on_surface),
                fontSize = 16.sp,
            ),
        )
        Text(
            text = value.ifBlank { " - " },
            style = TextStyle(
                color = colorResource(id = R.color.app_on_surface),
                fontSize = 16.sp,
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StockInfoDialogContentPreview() {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = colorResource(id = R.color.stock_item_background),
    ) {
        StockInfoDialogContent(
            code = "2330",
            name = "台積電",
            peRatio = "15.5",
            dividendYield = "1.2",
            pbRatio = "1.8",
            onDismiss = {},
        )
    }
}
