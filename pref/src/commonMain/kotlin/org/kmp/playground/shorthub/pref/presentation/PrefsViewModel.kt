package org.kmp.playground.shorthub.pref.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.kmp.playground.shorthub.db.domain.repository.ShortcutRepository
import org.kmp.playground.shorthub.shared.observation.InputObserver
import org.kmp.playground.shorthub.shared.ui.NavigationService

class PrefsViewModel(
    private val repository: ShortcutRepository,
    private val inputObserver: InputObserver,
    private val navigationService: NavigationService
) : ViewModel() {

    private val _state = MutableStateFlow(PrefsState())
    val state = _state.asStateFlow()

    init {
        repository.getPrefs()
            .onEach { prefs ->
                _state.update { it.copy(prefs = prefs) }
            }
            .launchIn(viewModelScope)

        inputObserver.keyEvents
            .onEach { event ->
                if (event.isPressed && _state.value.recordingTarget != null) {
                    handleRecordedKey(event)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun handleRecordedKey(event: org.kmp.playground.shorthub.shared.observation.KeyEvent) {
        val shortcut = buildString {
            if (event.ctrlPressed) append("Ctrl+")
            if (event.altPressed) append("Alt+")
            if (event.shiftPressed) append("Shift+")
            if (event.metaPressed) append("Meta+")
            append(event.key)
        }
        
        _state.update { it.copy(recordedShortcut = shortcut) }
    }

    fun saveRecordedShortcut() {
        val currentState = _state.value
        val shortcut = currentState.recordedShortcut ?: return
        
        when (currentState.recordingTarget) {
            RecordingTarget.AddShortcut -> onIntent(PrefsIntent.UpdateAddShortcut(shortcut))
            RecordingTarget.SearchShortcut -> onIntent(PrefsIntent.UpdateSearchShortcut(shortcut))
            null -> {}
        }
        
        stopRecording()
    }

    fun stopRecording() {
        _state.update { it.copy(recordingTarget = null, recordedShortcut = null) }
        inputObserver.stop()
        navigationService.setRecording(false)
    }

    fun onIntent(intent: PrefsIntent) {
        when (intent) {
            is PrefsIntent.UpdateAddShortcut -> {
                updatePrefs { it.copy(addNewShortcut = intent.shortcut) }
            }
            is PrefsIntent.UpdateSearchShortcut -> {
                updatePrefs { it.copy(searchShortcut = intent.shortcut) }
            }
        }
    }

    fun startRecording(target: RecordingTarget) {
        val currentShortcut = when (target) {
            RecordingTarget.AddShortcut -> _state.value.prefs.addNewShortcut
            RecordingTarget.SearchShortcut -> _state.value.prefs.searchShortcut
        }
        _state.update { it.copy(recordingTarget = target, recordedShortcut = currentShortcut) }
        inputObserver.start()
        navigationService.setRecording(true)
    }

    private fun updatePrefs(reducer: (org.kmp.playground.shorthub.db.domain.model.ShortcutPrefs) -> org.kmp.playground.shorthub.db.domain.model.ShortcutPrefs) {
        viewModelScope.launch {
            val currentPrefs = _state.value.prefs
            val newPrefs = reducer(currentPrefs)
            repository.savePrefs(newPrefs)
        }
    }
}
