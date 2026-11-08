package com.tvflux.espaceclient

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme(background = Color(0xFF0A0A0F), surface = Color(0xFF1E1E2A))) {
                Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF0A0A0F)) { AppNavigator() }
            }
        }
    }
}

@Composable
fun AppNavigator() {
    var currentScreen by remember { mutableStateOf("main") }
    when (currentScreen) {
        "main" -> MainScreen(onEnter = { currentScreen = "portal" })
        "portal" -> PortalWebView(url = "https://tvflux4k.com/p2p/customer_portal.php", onExit = { currentScreen = "main" })
    }
}

@Composable
fun MainScreen(onEnter: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFF15151F), Color(0xFF0A0A0F)))), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
            Text("فضاء الزبون", color = Color(0xFFF59E0B), fontSize = 36.sp, fontWeight = FontWeight.ExtraBold)
            Text("ESPACE CLIENT", color = Color(0xFF10B981), fontSize = 14.sp, fontWeight = FontWeight.Bold, letterSpacing = 4.sp, modifier = Modifier.padding(top = 8.dp, bottom = 48.dp))
            Button(onClick = onEnter, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))) {
                Text("دخول إلى فضاء الزبون", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PortalWebView(url: String, onExit: () -> Unit) {
    var webView by remember { mutableStateOf<WebView?>(null) }
    var showExitDialog by remember { mutableStateOf(false) }
    BackHandler { if (webView?.canGoBack() == true) webView?.goBack() else showExitDialog = true }
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth().background(Color(0xFF1E1E2A)).padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { if (webView?.canGoBack() == true) webView?.goBack() else showExitDialog = true }) { Icon(Icons.Default.ArrowBack, "رجوع", tint = Color(0xFFF59E0B)) }
            Text("فضاء الزبون", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            IconButton(onClick = { webView?.reload() }) { Icon(Icons.Default.Refresh, "تحديث", tint = Color.White) }
        }
        AndroidView(factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                settings.javaScriptEnabled = true; settings.domStorageEnabled = true
                webViewClient = WebViewClient(); loadUrl(url); webView = this
            }
        }, modifier = Modifier.weight(1f))
    }
    if (showExitDialog) {
        AlertDialog(onDismissRequest = { showExitDialog = false }, title = { Text("تأكيد الخروج", color = Color.White) }, text = { Text("هل تريد العودة للشاشة الرئيسية؟", color = Color(0xFF94A3B8)) },
            confirmButton = { TextButton(onClick = { showExitDialog = false; onExit() }) { Text("خروج", color = Color(0xFFEF4444), fontWeight = FontWeight.Bold) } },
            dismissButton = { TextButton(onClick = { showExitDialog = false }) { Text("إلغاء", color = Color.White) } },
            containerColor = Color(0xFF1E1E2A))
    }
}
