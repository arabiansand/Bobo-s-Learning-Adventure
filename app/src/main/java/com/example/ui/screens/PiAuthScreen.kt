package com.example.ui.screens

import android.annotation.SuppressLint
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

class PiWebAppInterface(
    private val onAuthSuccess: (String, String) -> Unit,
    private val onAuthError: (String) -> Unit
) {
    @JavascriptInterface
    fun onPiAuthenticated(accessToken: String, username: String) {
        onAuthSuccess(accessToken, username)
    }

    @JavascriptInterface
    fun onPiAuthError(error: String) {
        onAuthError(error)
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PiAuthScreen(
    onAuthComplete: (accessToken: String, username: String) -> Unit,
    onAuthError: (error: String) -> Unit,
    onNavigateBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text("Pi Network Authentication")
        }
        AndroidView(
            modifier = Modifier.weight(1f),
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    
                    webViewClient = WebViewClient()
                    webChromeClient = WebChromeClient()
                    
                    addJavascriptInterface(
                        PiWebAppInterface(onAuthComplete, onAuthError),
                        "Android"
                    )
                    
                    loadUrl("file:///android_asset/pi_auth.html")
                }
            }
        )
    }
}
