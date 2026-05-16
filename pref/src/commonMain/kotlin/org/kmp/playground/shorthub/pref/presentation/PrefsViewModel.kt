package org.kmp.playground.shorthub.pref.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.kmp.playground.shorthub.db.domain.repository.ShortcutRepository

class PrefsViewModel(
    private val repository: ShortcutRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PrefsState())
    val state = _state.asStateFlow()

    init {
        repository.getPrefs()
            .onEach { prefs ->
                _state.update { it.copy(prefs = prefs) }
            }
            .launchIn(viewModelScope)
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

    private fun updatePrefs(reducer: (org.kmp.playground.shorthub.db.domain.model.ShortcutPrefs) -> org.kmp.playground.shorthub.db.domain.model.ShortcutPrefs) {
        viewModelScope.launch {
            val currentPrefs = _state.value.prefs
            val newPrefs = reducer(currentPrefs)
            repository.savePrefs(newPrefs)
        }
    }
}
