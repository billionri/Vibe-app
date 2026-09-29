package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

fun formatDuration(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format("%d:%02d", m, s)
}

fun launchExternalLink(context: Context, urlOrQuery: String) {
    try {
        val uri = if (urlOrQuery.startsWith("http://") || urlOrQuery.startsWith("https://")) {
            Uri.parse(urlOrQuery)
        } else {
            Uri.parse("https://www.youtube.com/results?search_query=${Uri.encode(urlOrQuery)}")
        }
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Opening in browser...", Toast.LENGTH_SHORT).show()
    }
}
