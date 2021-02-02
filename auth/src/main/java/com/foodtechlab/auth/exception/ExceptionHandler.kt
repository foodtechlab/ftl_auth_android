package com.foodtechlab.auth.exception

import android.util.MalformedJsonException
import androidx.annotation.StringRes
import com.foodtechlab.auth.AuthManager
import com.foodtechlab.auth.BuildConfig
import com.foodtechlab.auth.R
import com.foodtechlab.auth.api.model.response.ErrorResponse
import com.foodtechlab.auth.api.model.response.PresentationData
import com.foodtechlab.auth.utils.logError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by Umalt on 1/14/21
 */

// domain
const val USER = "USER"
const val AUTH = "AUTH"

// details
const val UNKNOWN = "UNKNOWN"
const val TIMEOUT = "TIMEOUT"
const val NO_CONNECTION = "NO_CONNECTION"
const val TYPE_MALFORMED_JSON = "TYPE_MALFORMED_JSON"
const val PAGE_NOT_FOUND = "PAGE_NOT_FOUND"
const val INTERNAL_SERVER = "INTERNAL_SERVER"
const val INVALID_DATA = "INVALID_DATA"
const val INVALID_EMAIL = "INVALID_EMAIL"
const val INVALID_PASSWORD = "INVALID_PASSWORD"
const val UNAUTHORIZED = "UNAUTHORIZED"
const val UNSUCCESSFUL_REFRESH = "UNSUCCESSFUL_REFRESH"
const val INVALID_TOKEN_FORMAT = "INVALID_TOKEN_FORMAT"
const val ACCOUNT_IS_BLOCKED = "ACCOUNT_IS_BLOCKED"
const val EXPIRED_TOKEN = "EXPIRED_TOKEN"
const val NOT_EXIST = "NOT_EXIST"

interface ExceptionHandlerListener {

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

suspend fun <T : Any?> tryWithAuthChecking(
    authManager: AuthManager,
    block: suspend () -> T
): T? {
    return try {
        block()
    } catch (ex: HttpException) {
        if (BuildConfig.DEBUG) {
            logError(ex.message())
        }

        val error = ex.response()?.errorBody()?.stringSuspending()?.let { getErrorType(it) }

        when (ex.code()) {
            401 -> {
                if (error?.domain == AUTH) {
                    if (error.details == UNSUCCESSFUL_REFRESH) {
                        authManager.clearCache()
                    }
                    authManager.listener?.showMessage(
                        error.presentationData?.message,
                        error.presentationData?.title,
                        getString(R.string.common_ok),
                        showSupportButton = true,
                        cancellable = false,
                        shouldReauthorize = true
                    )
                }
                null
            }
            403 -> {
                when {
                    error?.domain == AUTH && error.details == EXPIRED_TOKEN -> {
                        try {
                            authManager.refresh()?.apply {
                                authManager.saveAccessToken(accessToken)
                                authManager.saveRefreshToken(refreshToken)
                            }
                            block()
                        } catch (e: HttpException) {
                            logError(e.message())
                            val err = e.formatError()
                            if (e.code() == 401 && err.first == UNSUCCESSFUL_REFRESH) {
                                authManager.listener?.showMessage(
                                    error.presentationData?.message,
                                    error.presentationData?.title,
                                    getString(R.string.common_login),
                                    cancellable = false,
                                    shouldReauthorize = true
                                )
                            }
                        }
                    }
                    error?.details == NOT_EXIST && error.domain == USER -> {
                        authManager.listener?.showMessage(
                            error.presentationData?.message,
                            error.presentationData?.title,
                            getString(R.string.common_login),
                            showSupportButton = true,
                            cancellable = false,
                            shouldReauthorize = true
                        )
                    }
                    else -> {
                        authManager.listener?.showMessage(
                            error?.presentationData?.message,
                            error?.presentationData?.title,
                            getString(R.string.common_ok),
                            showSupportButton = true,
                            cancellable = false,
                            shouldReauthorize = true
                        )
                    }
                }
                null
            }
            else -> {
                authManager.listener?.showMessage(
                    error?.presentationData?.message,
                    error?.presentationData?.title,
                    getString(R.string.common_ok)
                )
                null
            }
        }
    }
}


fun Exception.formatError(): Triple<String, String, String> {
    return if (this is HttpException) {
        val error = response()?.errorBody()?.string()?.let { getErrorType(it) }
        when (code()) {
            500 ->
                Triple(
                    error?.details ?: INTERNAL_SERVER,
                    error?.presentationData?.message ?: getString(R.string.error_connect),
                    error?.presentationData?.title ?: getString(R.string.error_title_connect)
                )
            404, 503 ->
                Triple(
                    error?.details ?: PAGE_NOT_FOUND,
                    error?.presentationData?.message ?: getString(R.string.error_internal_server),
                    error?.presentationData?.title ?: getString(R.string.error_title)
                )
            else ->
                Triple(
                    error?.details ?: UNKNOWN,
                    error?.presentationData?.message ?: getString(R.string.error_unknown),
                    error?.presentationData?.title ?: getString(R.string.error_title)
                )
        }
    } else if (this is SocketTimeoutException) {
        Triple(
            TIMEOUT,
            getString(R.string.error_connect),
            getString(R.string.error_title_timeout)
        )
    } else if (this is ConnectException || this is NoRouteToHostException || this is UnknownHostException) {
        Triple(
            NO_CONNECTION,
            getString(R.string.error_connect),
            getString(R.string.error_title_connect)
        )
    } else if (this is MalformedJsonException) {
        Triple(
            TYPE_MALFORMED_JSON,
            getString(R.string.error_malformed_json),
            getString(R.string.error_title)
        )
    } else {
        Triple(
            UNKNOWN,
            getString(R.string.error_unknown),
            getString(R.string.error_title)
        )
    }
}

/**
 * Получает детальную причину ошибки из JSON
 * @param errorJSON - входящий JSON
 * @return - локализованная причина ошибки
 */
fun getErrorType(errorJSON: String): ErrorResponse? {
    var errorResponse: ErrorResponse? = null
    try {
        val json = JSONObject(errorJSON)
        val errors = json.getJSONArray("errors")
        val error = errors.getJSONObject(0)
        val presentationData = error.getJSONObject("presentationData")

        errorResponse = ErrorResponse(
            error.getString("domain"),
            error.getString("details", UNKNOWN),
            PresentationData(
                presentationData.getString("title", getString(R.string.error_title)),
                presentationData.getString("message", getString(R.string.error_unknown))
            )
        )
    } catch (e: JSONException) {
        logError(e.message)
    }
    return errorResponse
}

fun JSONObject.getString(key: String, def: String): String {
    val result = optString(key)
    return when {
        result.isBlank() -> def
        else -> result
    }
}

fun getString(@StringRes id: Int, vararg parameters: Any): String {
    return AuthManager.applicationContext.getString(id, *parameters)
}

@Suppress("BlockingMethodInNonBlockingContext")
suspend fun ResponseBody.stringSuspending(): String {
    return withContext(Dispatchers.IO) { string() }
}