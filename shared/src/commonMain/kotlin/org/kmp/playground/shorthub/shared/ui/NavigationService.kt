package org.kmp.playground.shorthub.shared.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigationService {
    private val _isAddVisible = MutableStateFlow(false)
    val isAddVisible = _isAddVisible.asStateFlow()

    private val _isSearchVisible = MutableStateFlow(false)
    val isSearchVisible = _isSearchVisible.asStateFlow()

    private val _isRecording = MutableStateFlow(false)
    val isRecording = _isRecording.asStateFlow()

    fun showAdd() {
        _isAddVisible.value = true
        _isSearchVisible.value = false
    }

    fun showSearch() {
        _isSearchVisible.value = true
        _isAddVisible.value = false
    }

    fun hideAll() {
        _isAddVisible.value = false
        _isSearchVisible.value = false
        _isRecording.value = false
    }

    fun setRecording(recording: Boolean) {
        _isRecording.value = recording
    }
}
