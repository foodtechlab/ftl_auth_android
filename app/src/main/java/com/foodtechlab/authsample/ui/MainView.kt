package com.foodtechlab.authsample.ui

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

/**
 * Created by Umalt on 1/18/21
 */
interface MainView : MvpView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(
        message: String?,
        title: String? = null,
        posBtnText: String? = null,
        negBtnText: String? = null,
        negBtnAction: () -> Unit = {},
        posBtnAction: () -> Unit = {},
        showSupportButton: Boolean = false,
        cancellable: Boolean = true,
        shouldReauthorize: Boolean = false
    )
}