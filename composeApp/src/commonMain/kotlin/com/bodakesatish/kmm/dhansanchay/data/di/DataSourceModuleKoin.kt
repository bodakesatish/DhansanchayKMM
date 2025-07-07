package com.bodakesatish.kmm.dhansanchay.data.di

import com.bodakesatish.kmm.dhansanchay.data.repository.SchemeRepositoryImpl
import com.bodakesatish.kmm.dhansanchay.data.source.local.SchemeLocalDataSource
import com.bodakesatish.kmm.dhansanchay.data.source.local.SchemeLocalDataSourceImpl
import com.bodakesatish.kmm.dhansanchay.data.source.remote.SchemeRemoteDataSource
import com.bodakesatish.kmm.dhansanchay.data.source.remote.SchemeRemoteDataSourceImpl
import com.bodakesatish.kmm.dhansanchay.domain.repository.SchemeRepository
import org.koin.dsl.module

val dataSourceModuleKoin = module {

    single<SchemeRemoteDataSource> {
        SchemeRemoteDataSourceImpl(
            remoteApiService = get()
        )
    }

    single< SchemeLocalDataSource> {
        SchemeLocalDataSourceImpl(
            schemeQueries = get()
        )
    }

    single<SchemeRepository> {
        SchemeRepositoryImpl(
            remoteDataSource = get(),
            localDataSource = get()
        )
    }

}