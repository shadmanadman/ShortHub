package org.kmp.playground.shorthub.db.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kmp.playground.shorthub.db.data.local.ShortcutDao
import org.kmp.playground.shorthub.db.data.local.ShortcutEntity
import org.kmp.playground.shorthub.db.domain.model.Shortcut
import org.kmp.playground.shorthub.db.domain.repository.ShortcutRepository

class ShortcutRepositoryImpl(
    private val shortcutDao: ShortcutDao
) : ShortcutRepository {

    override suspend fun addShortcut(shortcut: Shortcut) {
        shortcutDao.insert(shortcut.toEntity())
    }

    override fun searchShortcuts(query: String): Flow<List<Shortcut>> {
        return shortcutDao.searchShortcuts(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    private fun Shortcut.toEntity() = ShortcutEntity(
        id = id,
        title = title,
        shortcut = shortcut
    )

    private fun ShortcutEntity.toDomain() = Shortcut(
        id = id,
        title = title,
        shortcut = shortcut
    )
}
