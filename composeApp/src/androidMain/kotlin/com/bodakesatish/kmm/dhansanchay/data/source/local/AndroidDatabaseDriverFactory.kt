package com.bodakesatish.kmm.dhansanchay.data.source.local

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.bodakesatish.kmm.dhansanchay.SchemeDatabase

class AndroidDatabaseDriverFactory(
    private val context: Context
): DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = SchemeDatabase.Schema,
            context = context,
            name = "mfschemes.db"
        )
    }
}