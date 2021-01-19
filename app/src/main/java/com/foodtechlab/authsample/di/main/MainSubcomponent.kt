package com.foodtechlab.authsample.di.main

import com.foodtechlab.authsample.ui.MainPresenter
import dagger.Subcomponent

/**
 * Created by Umalt on 1/18/21
 */
@MainScope
@Subcomponent(modules = [MainModule::class])
interface MainSubcomponent {
    fun inject(presenter: MainPresenter)
}