package com.bodakesatish.kmm.dhansanchay

import android.app.Application

class DhansanchayApp : Application() {
    override fun onCreate() {
        super.onCreate()
        KoinInitializer(applicationContext).init()
    }
}