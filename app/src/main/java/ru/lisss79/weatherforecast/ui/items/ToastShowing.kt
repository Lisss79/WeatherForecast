package ru.lisss79.weatherforecast.ui.items

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun ToastShowing(
    modifier: Modifier = Modifier,
    message: String?
) {
    val text = message ?: "Unknown error"
    Toast.makeText(LocalContext.current, text, Toast.LENGTH_SHORT).show()
}