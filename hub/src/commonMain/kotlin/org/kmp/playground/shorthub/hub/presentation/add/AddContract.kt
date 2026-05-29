package org.kmp.playground.shorthub.hub.presentation.add

data class AddState(
    val isVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isRecording: Boolean = false,
    val recordedShortcut: String = "",
    val error: String? = null
)

sealed interface AddIntent {
    data class Show(val show: Boolean) : AddIntent
    data class SaveShortcut(val title: String, val shortcut: String) : AddIntent
    data object Dismiss : AddIntent
}
