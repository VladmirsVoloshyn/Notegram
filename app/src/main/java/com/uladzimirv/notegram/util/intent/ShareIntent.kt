package com.uladzimirv.notegram.util.intent

import android.content.Context
import android.content.Intent

fun Context.sharePlainText(
    title: String,
    text: String,
    onError: (cause: String) -> Unit
) {
    runCatching {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        startActivity(Intent.createChooser(sendIntent, title))
    }.onFailure {
        onError(
            it.message.orEmpty()
        )
    }

}