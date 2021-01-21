package com.foodtechlab.authsample.di.main

import android.content.SharedPreferences
import com.foodtechlab.auth.AuthManager
import com.foodtechlab.authsample.BaseApplication
import com.foodtechlab.authsample.utils.net.ApiInterceptor
import dagger.Module
import dagger.Provides

/**
 * Created by Umalt on 1/18/21
 */
@Module
object MainModule {

    @Provides
    @MainScope
    fun provideAuthManager(prefs: SharedPreferences, apiInterceptor: ApiInterceptor): AuthManager {
        return AuthManager(
            baseUrl = "https://ftl-courier-api-stg1.sushivesla.net",
            apiVersion = "/not-secure/api/v1",
            sharedPrefs = prefs,
            apiInterceptor = apiInterceptor,
            applicationContext = BaseApplication.applicationContext
        )
    }
}