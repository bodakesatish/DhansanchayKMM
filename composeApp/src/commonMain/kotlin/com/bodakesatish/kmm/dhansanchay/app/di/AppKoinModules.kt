package com.bodakesatish.kmm.dhansanchay.app.di

import com.bodakesatish.kmm.dhansanchay.data.di.apiModule
import com.bodakesatish.kmm.dhansanchay.data.di.dataSourceModuleKoin
import com.bodakesatish.kmm.dhansanchay.data.di.databaseModule
import com.bodakesatish.kmm.dhansanchay.data.di.dispatchersModule
import com.bodakesatish.kmm.dhansanchay.data.di.networkModule
import org.koin.core.module.Module

expect val targetModule: Module
val appModules = listOf(dispatchersModule, networkModule, apiModule, targetModule, databaseModule, dataSourceModuleKoin, viewModelModule)

//fun initKoin() {
//    startKoin {
//        modules(
//            appModules
//        )
//    }
//}