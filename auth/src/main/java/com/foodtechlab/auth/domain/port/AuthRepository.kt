package com.foodtechlab.auth.domain.port

import com.foodtechlab.auth.domain.entity.AuthEntity

/**
 * Created by Umalt on 2/25/21
 */
interface AuthRepository {
    fun isAuthCompleted(): Boolean

    fun saveAccessToken(token: String?)

    fun saveRefreshToken(token: String?)

    fun getAccessToken(): String?

    fun getRefreshToken(): String?

    suspend fun initAuth(phone: String): Long?

    suspend fun loginSms(code: String, phone: String): AuthEntity?

    suspend fun loginPassword(email: String, password: String): AuthEntity?

    suspend fun refresh(): AuthEntity?

    suspend fun logout(): String?
}