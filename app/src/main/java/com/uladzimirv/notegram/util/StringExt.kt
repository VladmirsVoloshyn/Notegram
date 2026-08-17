package com.uladzimirv.notegram.util

inline fun String.ifNotEmpty(action: (String) -> Unit) {
    if (this.isNotEmpty()) {
        action(this)
    }
}