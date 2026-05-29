package org.kmp.playground.shorthub.hub.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import org.kmp.playground.shorthub.db.domain.repository.ShortcutRepository
import org.kmp.playground.shorthub.shared.observation.InputObserver
import org.kmp.playground.shorthub.shared.ui.NavigationService

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class SearchViewModel(
    private val repository: ShortcutRepository,
    private val inputObserver: InputObserver,
    private val navigationService: NavigationService
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    private val _query = MutableStateFlow("")

    init {
        navigationService.isSearchVisible
            .onEach { isVisible ->
                _state.update { it.copy(isVisible = isVisible) }
                if (isVisible) {
                    inputObserver.start()
                }
            }
            .launchIn(viewModelScope)

        _query
            .debounce(300)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                if (query.isBlank()) {
                    flowOf(emptyList())
                } else {
                    repository.searchShortcuts(query)
                }
            }
            .onEach { results ->
                _state.update { it.copy(results = results.take(10), isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    fun onIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.Show -> navigationService.showSearch()
            is SearchIntent.Dismiss -> navigationService.hideAll()
            is SearchIntent.UpdateQuery -> {
                _state.update { it.copy(query = intent.query, isLoading = true) }
                _query.value = intent.query
            }
        }
    }
}
