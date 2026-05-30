package org.kmp.playground.shorthub.shared.di

import org.kmp.playground.shorthub.shared.observation.InputObserver
import org.kmp.playground.shorthub.shared.observation.createInputObserver
import org.kmp.playground.shorthub.shared.ui.NavigationService
import org.koin.dsl.module

val sharedModule = module {
    single<InputObserver> { createInputObserver() }
    single { NavigationService() }
}
