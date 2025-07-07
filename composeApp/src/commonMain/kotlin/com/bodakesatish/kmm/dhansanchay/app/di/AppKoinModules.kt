package com.bodakesatish.kmm.dhansanchay.app.di

import com.bodakesatish.kmm.dhansanchay.data.di.apiModule
import com.bodakesatish.kmm.dhansanchay.data.di.dataSourceModuleKoin
import com.bodakesatish.kmm.dhansanchay.data.di.dispatchersModule
import com.bodakesatish.kmm.dhansanchay.data.di.networkModule

val appModules = listOf(dispatchersModule, networkModule, apiModule, dataSourceModuleKoin, viewModelModule)

//fun initKoin() {
//    startKoin {
//        modules(
//            appModules
//        )
//    }
//}