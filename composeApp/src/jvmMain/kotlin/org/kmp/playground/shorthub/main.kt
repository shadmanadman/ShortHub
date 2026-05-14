package org.kmp.playground.shorthub

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ShortHub",
    ) {
        App()
    }
}