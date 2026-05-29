package org.kmp.playground.shorthub.pref.di

import org.kmp.playground.shorthub.pref.presentation.PrefsViewModel
import org.koin.dsl.module

val prefModule = module {
    factory { PrefsViewModel(get(), get(), get()) }
}
