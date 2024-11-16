package ru.lisss79.weatherforecast.ui.items.weatheritem

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun UpFAB(
    modifier: Modifier = Modifier,
    localScope: CoroutineScope,
    columnState: LazyListState
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = {
            localScope.launch {
                columnState.animateScrollToItem(0)
            }
        }
    ) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowUp,
            contentDescription = "up"
        )
    }
}