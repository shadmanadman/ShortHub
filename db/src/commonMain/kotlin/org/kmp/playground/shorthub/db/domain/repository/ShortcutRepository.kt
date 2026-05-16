package org.kmp.playground.shorthub.db.domain.repository

import kotlinx.coroutines.flow.Flow
import org.kmp.playground.shorthub.db.domain.model.Shortcut

interface ShortcutRepository {
    suspend fun addShortcut(shortcut: Shortcut)
    fun searchShortcuts(query: String): Flow<List<Shortcut>>
}
