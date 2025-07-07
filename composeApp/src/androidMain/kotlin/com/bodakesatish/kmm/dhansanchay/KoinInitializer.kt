package com.bodakesatish.kmm.dhansanchay

import android.content.Context
import com.bodakesatish.kmm.dhansanchay.app.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

actual class KoinInitializer(
    private val context: Context
) {
    actual fun init() {
        startKoin {
            androidContext(context)
            androidLogger()
            modules(
                appModules
            )
        }
    }
}