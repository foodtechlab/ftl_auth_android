package com.foodtechlab.authsample.di

import com.foodtechlab.authsample.di.application.ApplicationComponent
import com.foodtechlab.authsample.di.main.MainModule
import com.foodtechlab.authsample.di.main.MainSubcomponent

/**
 * Created by Umalt on 1/18/21
 */
object DIManager {

    lateinit var applicationComponent: ApplicationComponent

    private var mainSubcomponent: MainSubcomponent? = null

    fun removeMainSubcomponent() {
        mainSubcomponent = null
    }

    fun addMainSubcomponent(): MainSubcomponent {
        if (mainSubcomponent == null) {
            mainSubcomponent = applicationComponent.addMainSubcomponent(MainModule)
        }
        return mainSubcomponent ?: throw IllegalStateException("$mainSubcomponent must not be null")
    }
}