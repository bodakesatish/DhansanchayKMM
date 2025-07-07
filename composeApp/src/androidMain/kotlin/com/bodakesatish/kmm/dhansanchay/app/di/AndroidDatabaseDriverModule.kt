package com.bodakesatish.kmm.dhansanchay.app.di

import com.bodakesatish.kmm.dhansanchay.data.source.local.AndroidDatabaseDriverFactory
import com.bodakesatish.kmm.dhansanchay.data.source.local.DatabaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val targetModule = module {
    single<DatabaseDriverFactory> {
        AndroidDatabaseDriverFactory(androidContext())
    }

}