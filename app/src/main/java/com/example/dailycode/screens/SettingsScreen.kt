package com.example.dailycode.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun SettingsScreen(){
    Text(
        modifier = Modifier.fillMaxSize().wrapContentHeight(),
        text = "Settings",
        textAlign = TextAlign.Center
    )
}