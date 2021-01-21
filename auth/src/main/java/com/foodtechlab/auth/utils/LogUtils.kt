package com.foodtechlab.auth.utils

import android.util.Log
import com.foodtechlab.auth.BuildConfig

/**
 * Created by Umalt on 1/14/21
 */

private const val LOG_TAG = "AuthManager"
private const val DEFAULT_MESSAGE = "Undefined error"

fun Throwable.logError(message: String?, tag: String? = null) {
    if (BuildConfig.DEBUG) {
        Log.e(tag ?: LOG_TAG, message ?: DEFAULT_MESSAGE, this)
    }
}

fun logError(message: String?, tag: String? = null) {
    if (BuildConfig.DEBUG) {
        Log.e(tag ?: LOG_TAG, message ?: DEFAULT_MESSAGE)
    }
}

fun logDebug(message: String?, tag: String? = null) {
    if (BuildConfig.DEBUG) {
        Log.d(tag ?: LOG_TAG, message ?: DEFAULT_MESSAGE)
    }
}