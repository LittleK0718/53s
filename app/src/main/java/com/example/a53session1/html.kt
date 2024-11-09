package com.example.a53session1

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView


@Composable
fun WebViewScreen(htmlFile: String?) {
    val context = LocalContext.current
    AndroidView(factory = { WebView(context).apply {
        loadUrl("file:///android_asset/$htmlFile")
    } })
}
