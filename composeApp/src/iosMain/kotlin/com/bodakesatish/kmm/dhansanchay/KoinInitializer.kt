package com.bodakesatish.kmm.dhansanchay

import com.bodakesatish.kmm.dhansanchay.app.di.appModules
import org.koin.core.context.startKoin

actual class KoinInitializer {
    actual fun init() {
        startKoin {
            modules(
                appModules
            )
        }
    }
}