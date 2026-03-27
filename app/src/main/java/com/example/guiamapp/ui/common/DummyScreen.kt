package com.example.guiamapp.ui.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DummyScreen(title: String) {
    Text(text = title, style = MaterialTheme.typography.headlineMedium)
}