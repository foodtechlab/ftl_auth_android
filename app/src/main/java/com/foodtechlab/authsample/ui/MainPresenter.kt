package com.foodtechlab.authsample.ui

import com.foodtechlab.auth.AuthManager
import com.foodtechlab.auth.exception.ExceptionHandlerListener
import com.foodtechlab.auth.exception.formatError
import com.foodtechlab.auth.utils.logError
import com.foodtechlab.authsample.di.DIManager
import kotlinx.coroutines.launch
import moxy.MvpPresenter
import moxy.presenterScope
import javax.inject.Inject

/**
 * Created by Umalt on 1/18/21
 */
class MainPresenter : MvpPresenter<MainView>(), ExceptionHandlerListener {

    @Inject
    lateinit var authManager: AuthManager

    init {
        DIManager.addMainSubcomponent().inject(this)
    }

    override fun onDestroy() {
        DIManager.removeMainSubcomponent()
        super.onDestroy()
    }

    override fun onFirstViewAttach() {
        authManager.listener = this
        authSms()
    }

    override fun showMessage(
        message: String?,
        title: String?,
        posBtnText: String?,
        negBtnText: String?,
        negBtnAction: () -> Unit,
        posBtnAction: () -> Unit,
        showSupportButton: Boolean,
        cancellable: Boolean,
        shouldReauthorize: Boolean
    ) {
        presenterScope.launch {
            viewState.showMessage(
                message,
                title,
                posBtnText,
                negBtnText,
                negBtnAction,
                posBtnAction,
                showSupportButton,
                cancellable,
                shouldReauthorize
            )
        }
    }

    private fun authSms() {
        presenterScope.launch {
            try {
                authManager.authSms("89029999999")
                authManager.loginSms("1234", "89029999999")
            } catch (e: Exception) {
                logError(TAG, e.formatError().second)
            }
        }
    }

    companion object {
        private const val TAG = "MainPresenter"
    }
}