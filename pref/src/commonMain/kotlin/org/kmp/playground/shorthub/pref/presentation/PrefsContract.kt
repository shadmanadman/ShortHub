package org.kmp.playground.shorthub.pref.presentation

import org.kmp.playground.shorthub.db.domain.model.ShortcutPrefs

data class PrefsState(
    val prefs: ShortcutPrefs = ShortcutPrefs(),
    val isLoading: Boolean = false
)

sealed interface PrefsIntent {
    data class UpdateAddShortcut(val shortcut: String) : PrefsIntent
    data class UpdateSearchShortcut(val shortcut: String) : PrefsIntent
}
