package com.bodakesatish.kmm.dhansanchay.data.source.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.bodakesatish.kmm.dhansanchay.SchemeDatabase

class IOSDatabaseDriverFactory(): DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = SchemeDatabase.Schema,
            name = "mfschemes.db"
        )
    }
}