package com.foodtechlab.auth

import android.content.SharedPreferences
import com.foodtechlab.auth.api.model.request.AuthRequest
import com.foodtechlab.auth.api.model.request.RefreshRequest
import com.foodtechlab.auth.api.model.response.AuthResponse
import com.foodtechlab.auth.api.model.response.TimerResponse
import com.foodtechlab.auth.api.model.response.Token
import com.foodtechlab.auth.api.service.AuthApiServiceFactory
import com.foodtechlab.auth.cache.AuthPrefsCache
import com.foodtechlab.auth.exception.ExceptionHandlerListener
import com.foodtechlab.auth.exception.formatError
import com.foodtechlab.auth.exception.getString
import com.foodtechlab.auth.exception.tryWithAuthChecking
import com.foodtechlab.auth.utils.logError
import okhttp3.Interceptor

/**
 * Created by Umalt on 1/14/21
 */
class AuthManager constructor(
    private val baseUrl: String,
    private val apiVersion: String,
    private val sharedPrefs: SharedPreferences,
    private val listener: ExceptionHandlerListener,
    private val apiInterceptor: Interceptor? = null
) {

    val isAuthCompleted: Boolean
        get() = authCache.isAuthCompleted()

    private val authCache by lazy { AuthPrefsCache(sharedPrefs) }

    private val authApiService by lazy {
        AuthApiServiceFactory.makeAuthApiService(baseUrl, apiVersion, apiInterceptor)
    }

    suspend fun authSms(phone: String): TimerResponse? {
        return try {
            tryWithAuthChecking(this, listener) {
                authApiService.authSms(AuthRequest(phoneNumber = phone))
            }?.result
        } catch (e: Exception) {
            logError(e.message)
            val err = e.formatError()
            listener.showMessage(err.third, err.second, getString(R.string.common_ok))
            null
        }
    }

    suspend fun loginSms(code: String, phone: String): AuthResponse? {
        return try {
            tryWithAuthChecking(this, listener) {
                authApiService.loginSms(AuthRequest(phoneNumber = phone, code = code))
            }?.result?.apply {
                saveAccessToken(accessToken)
                saveRefreshToken(refreshToken)
            }
        } catch (e: Exception) {
            logError(e.message)
            val err = e.formatError()
            listener.showMessage(err.third, err.second, getString(R.string.common_ok))
            null
        }
    }

    suspend fun loginPassword(email: String, password: String): AuthResponse? {
        return try {
            tryWithAuthChecking(this, listener) {
                authApiService.loginPassword(AuthRequest(email = email, password = password))
            }?.result?.apply {
                saveAccessToken(accessToken)
                saveRefreshToken(refreshToken)
            }
        } catch (e: Exception) {
            logError(e.message)
            val err = e.formatError()
            listener.showMessage(err.third, err.second, getString(R.string.common_ok))
            null
        }
    }

    suspend fun refresh(): AuthResponse? {
        return try {
            tryWithAuthChecking(this, listener) {
                authApiService.refresh(RefreshRequest(authCache.getRefreshToken()?.token))
            }?.result?.apply {
                saveAccessToken(accessToken)
                saveRefreshToken(refreshToken)
            }
        } catch (e: Exception) {
            logError(e.message)
            val err = e.formatError()
            listener.showMessage(err.third, err.second, getString(R.string.common_ok))
            null
        }
    }

    suspend fun logout(): String? {
        return try {
            tryWithAuthChecking(this, listener) {
                authApiService.logout()
            }?.result?.apply {
                clearCache()
            }
        } catch (e: Exception) {
            logError(e.message)
            val err = e.formatError()
            listener.showMessage(err.third, err.second, getString(R.string.common_ok))
            null
        }
    }

    internal fun saveAccessToken(token: Token) {
        authCache.saveRefreshToken(token)
    }

    internal fun saveRefreshToken(token: Token) {
        authCache.saveRefreshToken(token)
    }

    internal fun clearCache() {
        authCache.clear()
    }
}