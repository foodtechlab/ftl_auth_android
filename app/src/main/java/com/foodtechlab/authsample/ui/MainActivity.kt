package com.foodtechlab.authsample.ui

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.foodtechlab.authsample.R
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter

class MainActivity : MvpAppCompatActivity(), MainView {

    private val presenter by moxyPresenter { MainPresenter() }

    private var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
        if (alertDialog?.isShowing != true) {
            alertDialog = AlertDialog.Builder(this).apply {
                title?.let { setTitle(it) }
                setMessage(message)
                setPositiveButton(posBtnText) { _, _ -> posBtnAction() }
                negBtnText?.let { setNegativeButton(it) { _, _ -> negBtnAction() } }
                setCancelable(cancellable)
            }.create()

            alertDialog?.show()
        }
    }
}