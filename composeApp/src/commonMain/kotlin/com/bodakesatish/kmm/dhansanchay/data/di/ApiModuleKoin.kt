package com.bodakesatish.kmm.dhansanchay.data.di

import com.bodakesatish.kmm.dhansanchay.data.source.remote.api.SchemeApiService
import org.koin.core.qualifier.named
import org.koin.dsl.module

val apiModule = module {
    single {
        SchemeApiService(
            httpClient = get(),
            ioDispatcher = get(named("IODispatcher"))
        )
    }
}