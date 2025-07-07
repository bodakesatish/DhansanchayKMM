package com.bodakesatish.kmm.dhansanchay.app.di

import com.bodakesatish.kmm.dhansanchay.app.screens.list.ListViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val viewModelModule = module {
    singleOf(::ListViewModel)
}