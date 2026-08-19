package com.uladzimirv.notegram.util.intent

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun Context.openLinkInBrowser(
    url: String,
    onError: (cause: String) -> Unit
) {
    runCatching {
        startActivity(
            Intent(
                Intent.ACTION_VIEW, url.toUri()
            )
        )
    }.onFailure {
        onError(
            it.message.orEmpty()
        )
    }

}