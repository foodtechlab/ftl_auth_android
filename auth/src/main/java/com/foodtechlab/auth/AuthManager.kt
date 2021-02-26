package com.foodtechlab.auth

import android.content.Context
import android.content.SharedPreferences
import com.foodtechlab.auth.api.model.request.AuthRequest
import com.foodtechlab.auth.api.model.request.RefreshRequest
import com.foodtechlab.auth.api.model.response.AuthResponse
import com.foodtechlab.auth.api.model.response.TimerResponse
import com.foodtechlab.auth.api.service.AuthApiServiceFactory
import com.foodtechlab.auth.cache.AuthPrefsCache
import com.foodtechlab.auth.exception.ExceptionHandlerListener
import com.foodtechlab.auth.exception.formatError
import com.foodtechlab.auth.exception.getString
import com.foodtechlab.auth.exception.tryWithAuthChecking
import com.foodtechlab.auth.utils.logError
import okhttp3.OkHttpClient

/**
 * Created by Umalt on 1/14/21
 */
class AuthManager constructor(
    private val baseUrl: String,
    private val apiVersion: String,
    private val sharedPrefs: SharedPreferences,
    private val okHttpClient: OkHttpClient,
    applicationContext: Context
) {

    val isAuthCompleted: Boolean
        get() = authCache.isAuthCompleted()

    val accessToken: String?
        get() = authCache.getAccessToken()

    val refreshToken: String?
        get() = authCache.getRefreshToken()

    var listener: ExceptionHandlerListener? = null

    private val authCache by lazy { AuthPrefsCache(sharedPrefs) }

    private val authApiService by lazy {
        AuthApiServiceFactory.makeAuthApiService(baseUrl, okHttpClient)
    }

    init {
        AuthManager.applicationContext = applicationContext
    }

    suspend fun initSms(phone: String): TimerResponse? {
        return try {
            tryWithAuthChecking(this) {
                authApiService.authSms(apiVersion, AuthRequest(phoneNumber = phone))
            }?.result
        } catch (e: Exception) {
            logError(e.message)
            val err = e.formatError()
            listener?.showMessage(err.second, err.third, getString(R.string.common_ok))
            null
        }
    }

    suspend fun loginSms(code: String, phone: String): AuthResponse? {
        return try {
            tryWithAuthChecking(this) {
                authApiService.loginSms(apiVersion, AuthRequest(phoneNumber = phone, code = code))
            }?.result?.apply {
                saveAccessToken(accessToken)
                saveRefreshToken(refreshToken)
            }
        } catch (e: Exception) {
            logError(e.message)
            val err = e.formatError()
            listener?.showMessage(err.second, err.third, getString(R.string.common_ok))
            null
        }
    }

    suspend fun loginPassword(email: String, password: String): AuthResponse? {
        return try {
            tryWithAuthChecking(this) {
                authApiService.loginPassword(
                    apiVersion,
                    AuthRequest(email = email, password = password)
                )
            }?.result?.apply {
                saveAccessToken(accessToken)
                saveRefreshToken(refreshToken)
            }
        } catch (e: Exception) {
            logError(e.message)
            val err = e.formatError()
            listener?.showMessage(err.second, err.third, getString(R.string.common_ok))
            null
        }
    }

    suspend fun refresh(): AuthResponse? {
        return try {
            tryWithAuthChecking(this) {
                authApiService.refresh(apiVersion, RefreshRequest(authCache.getRefreshToken()))
            }?.result?.apply {
                saveAccessToken(accessToken)
                saveRefreshToken(refreshToken)
            }
        } catch (e: Exception) {
            logError(e.message)
            val err = e.formatError()
            listener?.showMessage(err.second, err.third, getString(R.string.common_ok))
            null
        }
    }

    suspend fun logout(): String? {
        return try {
            tryWithAuthChecking(this) {
                authApiService.logout(apiVersion)
            }?.result?.apply {
                clearCache()
            }
        } catch (e: Exception) {
            logError(e.message)
            val err = e.formatError()
            listener?.showMessage(err.second, err.third, getString(R.string.common_ok))
            null
        }
    }

    fun saveAccessToken(token: String?) {
        authCache.saveAccessToken(token)
    }

    fun saveRefreshToken(token: String?) {
        authCache.saveRefreshToken(token)
    }

    fun clearCache() {
        authCache.clear()
    }

    companion object {
        internal lateinit var applicationContext: Context
    }
}