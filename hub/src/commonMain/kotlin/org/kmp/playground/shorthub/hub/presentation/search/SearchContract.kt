package org.kmp.playground.shorthub.hub.presentation.search

import org.kmp.playground.shorthub.db.domain.model.Shortcut

data class SearchState(
    val isVisible: Boolean = false,
    val query: String = "",
    val results: List<Shortcut> = emptyList(),
    val isLoading: Boolean = false
)

sealed interface SearchIntent {
    data class Show(val show: Boolean) : SearchIntent
    data class UpdateQuery(val query: String) : SearchIntent
    data object Dismiss : SearchIntent
}
