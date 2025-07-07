package com.bodakesatish.kmm.dhansanchay.data.di

import com.bodakesatish.kmm.dhansanchay.SchemeDatabase
import com.bodakesatish.kmm.dhansanchay.SchemeEntityQueries
import com.bodakesatish.kmm.dhansanchay.data.source.local.DatabaseDriverFactory
import org.koin.dsl.module

/**
 * Koin module for providing database-related dependencies.
 */
val databaseModule = module {

    // Provide the AppDatabase instance
    single<SchemeDatabase> {
        val driverFactory = get<DatabaseDriverFactory>() // Get the factory
        SchemeDatabase(driver = driverFactory.createDriver()) // 'get()' resolves SqlDriver
    }

    // Provide the SchemeEntityQueries (for interacting with the SchemeEntity table)
    single<SchemeEntityQueries> {
        val database = get<SchemeDatabase>() // Get the AppDatabase instance
        database.schemeEntityQueries // Access the generated queries property
    }
}