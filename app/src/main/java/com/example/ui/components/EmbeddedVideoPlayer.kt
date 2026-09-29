package com.example.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.model.StoreMovie
import com.example.ui.theme.VibeAccent
import com.example.ui.theme.VibeBorder
import com.example.ui.theme.VibeCard
import com.example.ui.theme.VibeGold
import com.example.ui.theme.VibeMuted
import com.example.ui.theme.VibeSurface
import com.example.ui.theme.VibeText
import com.example.util.launchExternalLink

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun VideoPlayerView(
    videoId: String,
    modifier: Modifier = Modifier
) {
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var hasError by remember { mutableStateOf(false) }

    DisposableEffect(videoId) {
        onDispose {
            try {
                webViewRef?.loadUrl("about:blank")
                webViewRef?.destroy()
            } catch (_: Exception) {}
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        if (!hasError) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.mediaPlaybackRequiresUserGesture = false
                        webChromeClient = WebChromeClient()
                        webViewClient = object : WebViewClient() {
                            override fun onReceivedError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                error: WebResourceError?
                            ) {
                                super.onReceivedError(view, request, error)
                                if (request?.isForMainFrame == true) {
                                    hasError = true
                                }
                            }
                        }
                        webViewRef = this

                        val embedHtml = """
                            <!DOCTYPE html>
                            <html>
                            <head>
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <style>
                              * { margin:0; padding:0; box-sizing:border-box; }
                              body { background:#0a0a0f; display:flex; align-items:center; justify-content:center; height:100vh; width:100vw; overflow:hidden; }
                              iframe { width:100%; height:100%; border:none; }
                            </style>
                            </head>
                            <body>
                            <iframe src="https://www.youtube.com/embed/$videoId?autoplay=1&enablejsapi=1&rel=0&playsinline=1"
                                    allow="autoplay; encrypted-media; picture-in-picture"
                                    allowfullscreen></iframe>
                            </body>
                            </html>
                        """.trimIndent()

                        loadDataWithBaseURL("https://www.youtube.com", embedHtml, "text/html", "UTF-8", null)
                    }
                },
                update = { webView ->
                    val embedHtml = """
                        <!DOCTYPE html>
                        <html>
                        <head>
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <style>
                          * { margin:0; padding:0; box-sizing:border-box; }
                          body { background:#0a0a0f; display:flex; align-items:center; justify-content:center; height:100vh; width:100vw; overflow:hidden; }
                          iframe { width:100%; height:100%; border:none; }
                        </style>
                        </head>
                        <body>
                        <iframe src="https://www.youtube.com/embed/$videoId?autoplay=1&enablejsapi=1&rel=0&playsinline=1"
                                allow="autoplay; encrypted-media; picture-in-picture"
                                allowfullscreen></iframe>
                        </body>
                        </html>
                    """.trimIndent()
                    webView.loadDataWithBaseURL("https://www.youtube.com", embedHtml, "text/html", "UTF-8", null)
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("🎬 Video Preview", color = VibeText, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Tap below to open directly:", color = VibeMuted, fontSize = 11.sp)
                Spacer(modifier = Modifier.height(8.dp))
                val ctx = LocalContext.current
                Button(
                    onClick = { launchExternalLink(ctx, "https://www.youtube.com/watch?v=$videoId") },
                    colors = ButtonDefaults.buttonColors(containerColor = VibeAccent)
                ) {
                    Text("Open on YouTube")
                }
            }
        }
    }
}

@Composable
fun TrailerPlayerModal(
    movie: StoreMovie,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = VibeSurface,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "🎬 ${movie.title} (${movie.year})",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = VibeText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Official Trailer · IMDb ${movie.rating}★",
                        fontSize = 11.sp,
                        color = VibeGold
                    )
                }
                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = VibeMuted)
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                VideoPlayerView(
                    videoId = movie.id,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Collection: ${movie.collection} • ${movie.genre}",
                        fontSize = 11.sp,
                        color = VibeMuted
                    )
                    TextButton(
                        onClick = { launchExternalLink(context, "https://www.youtube.com/watch?v=${movie.id}") }
                    ) {
                        Icon(Icons.Default.OpenInNew, contentDescription = null, tint = VibeAccent, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.size(3.dp))
                        Text("YouTube", fontSize = 11.sp, color = VibeAccent)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = VibeAccent, contentColor = Color.White)
            ) {
                Text("Close")
            }
        },
        dismissButton = null
    )
}
