package org.kmp.playground.shorthub

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.kmp.playground.shorthub.db.di.dbModule
import org.kmp.playground.shorthub.hub.di.hubModule
import org.kmp.playground.shorthub.pref.di.prefModule
import org.kmp.playground.shorthub.db.domain.repository.ShortcutRepository
import org.kmp.playground.shorthub.shared.di.sharedModule
import org.kmp.playground.shorthub.shared.observation.InputObserver
import org.kmp.playground.shorthub.shared.observation.ShortcutMatcher
import org.kmp.playground.shorthub.shared.ui.NavigationService
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun main() {
    startKoin {
        modules(dbModule, hubModule, prefModule, sharedModule)
    }
    
    val inputObserver = getKoin().get<InputObserver>()
    val navigationService = getKoin().get<NavigationService>()
    val repository = getKoin().get<ShortcutRepository>()
    val scope = MainScope()

    // Global Shortcut Handler
    combine(
        repository.getPrefs(),
        inputObserver.keyEvents
    ) { prefs, event ->
        if (ShortcutMatcher.matches(event, prefs.addNewShortcut)) {
            navigationService.showAdd()
        } else if (ShortcutMatcher.matches(event, prefs.searchShortcut)) {
            navigationService.showSearch()
        }
    }.launchIn(scope)

    inputObserver.start()

    application {
        Window(
            onCloseRequest = {
                inputObserver.stop()
                exitApplication()
            },
            title = "ShortHub",
        ) {
            App()
        }
    }
}
