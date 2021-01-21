package com.foodtechlab.authsample.di.application

import android.content.Context
import com.foodtechlab.authsample.di.main.MainModule
import com.foodtechlab.authsample.di.main.MainSubcomponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Umalt on 1/18/21
 */
@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun addMainSubcomponent(module: MainModule): MainSubcomponent

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): ApplicationComponent
    }
}