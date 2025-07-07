package com.bodakesatish.kmm.dhansanchay.app.di

import com.bodakesatish.kmm.dhansanchay.app.screens.list.ListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

actual val viewModelModule = module {
    viewModelOf(::ListViewModel)
}