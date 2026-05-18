package org.kmp.playground.shorthub.db.di

import org.kmp.playground.shorthub.db.data.local.AppDatabase
import org.kmp.playground.shorthub.db.data.local.PrefDao
import org.kmp.playground.shorthub.db.data.local.ShortcutDao
import org.kmp.playground.shorthub.db.data.local.getDatabaseBuilder
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.kmp.playground.shorthub.db.data.repository.ShortcutRepositoryImpl
import org.kmp.playground.shorthub.db.domain.repository.ShortcutRepository
import org.koin.dsl.module

val dbModule = module {
    single<AppDatabase> {
        getDatabaseBuilder()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    
    single<ShortcutDao> { get<AppDatabase>().shortcutDao() }
    single<PrefDao> { get<AppDatabase>().prefDao() }
    
    single<ShortcutRepository> { ShortcutRepositoryImpl(get(), get()) }
}
