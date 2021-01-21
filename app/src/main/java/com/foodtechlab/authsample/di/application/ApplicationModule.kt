package com.foodtechlab.authsample.di.application

import android.content.Context
import android.content.SharedPreferences
import com.foodtechlab.authsample.utils.KEY_FTL_AUTH_PREFS_SETTINGS
import com.foodtechlab.authsample.utils.net.ApiInterceptor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Umalt on 1/18/21
 */
@Module
object ApplicationModule {

    @Singleton
    @Provides
    fun provideSharedPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            KEY_FTL_AUTH_PREFS_SETTINGS,
            Context.MODE_PRIVATE
        )
    }

    @Provides
    @Singleton
    fun provideApiInterceptor(): ApiInterceptor {
        return ApiInterceptor()
    }
}