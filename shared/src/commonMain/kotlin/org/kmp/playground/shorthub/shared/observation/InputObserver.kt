package org.kmp.playground.shorthub.shared.observation

import kotlinx.coroutines.flow.Flow

interface InputObserver {
    val keyEvents: Flow<KeyEvent>
    fun start()
    fun stop()
}

data class KeyEvent(
    val key: String,
    val isPressed: Boolean,
    val ctrlPressed: Boolean = false,
    val altPressed: Boolean = false,
    val shiftPressed: Boolean = false,
    val metaPressed: Boolean = false
)

expect fun createInputObserver(): InputObserver
