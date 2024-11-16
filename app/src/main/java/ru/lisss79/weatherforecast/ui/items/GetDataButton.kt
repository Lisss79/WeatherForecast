package ru.lisss79.weatherforecast.ui.items

import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun GetDataButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
    ) {
    ElevatedButton(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick
    ) {
        Text(text = "Get data!")
    }
}

@Preview
@Composable
fun GetDataButtonPreview() {
    GetDataButton { }
}
