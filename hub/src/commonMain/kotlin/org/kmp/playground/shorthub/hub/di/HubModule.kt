package org.kmp.playground.shorthub.hub.di

import org.kmp.playground.shorthub.hub.presentation.add.AddViewModel
import org.kmp.playground.shorthub.hub.presentation.search.SearchViewModel
import org.koin.dsl.module

val hubModule = module {
    factory { AddViewModel(get(), get(), get()) }
    factory { SearchViewModel(get(), get(), get()) }
}
