package com.bodakesatish.kmm.dhansanchay.data.source.local

import app.cash.sqldelight.db.SqlDriver
import com.bodakesatish.kmm.dhansanchay.SchemeDatabase

interface DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

class LocalDatabase(
    databaseDriverFactory: DatabaseDriverFactory
) {
    val database = SchemeDatabase(
        driver = databaseDriverFactory.createDriver()
    )
    private val query = database.schemeEntityQueries


}