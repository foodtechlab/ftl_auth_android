package com.foodtechlab.authsample

import android.app.Application
import android.content.Context
import com.foodtechlab.authsample.di.DIManager
import com.foodtechlab.authsample.di.application.DaggerApplicationComponent

/**
 * Created by Umalt on 1/18/21
 */
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        BaseApplication.applicationContext = applicationContext

        DIManager.applicationComponent = DaggerApplicationComponent.builder()
            .context(this)
            .build()
    }

    companion object {
        lateinit var applicationContext: Context
    }
}