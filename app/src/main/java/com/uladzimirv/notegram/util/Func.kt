package com.uladzimirv.notegram.util

fun VEVO(value: Any?) {
    if (value is Iterable<Any?>) {
        value.forEach {
            println("VEVO iterable element ${it.toString()}")
        }
    } else {
        println("VEVO ${value.toString()}")
    }
}