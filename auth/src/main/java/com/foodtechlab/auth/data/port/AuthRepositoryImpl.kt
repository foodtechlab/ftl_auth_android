package com.foodtechlab.auth.data.port

import com.foodtechlab.auth.AuthManager
import com.foodtechlab.auth.data.mapper.AuthEntityMapper
import com.foodtechlab.auth.domain.entity.AuthEntity
import com.foodtechlab.auth.domain.port.AuthRepository

/**
 * Created by Umalt on 2/25/21
 */
class AuthRepositoryImpl(
    private val authManager: AuthManager,
    private val mapper: AuthEntityMapper
) : AuthRepository {
    override fun isAuthCompleted(): Boolean {
        return authManager.isAuthCompleted
    }

    override fun saveAccessToken(token: String?) {
        authManager.saveAccessToken(token)
    }

    override fun saveRefreshToken(token: String?) {
        authManager.saveRefreshToken(token)
    }

    override fun getAccessToken(): String? {
        return authManager.accessToken
    }

    override fun getRefreshToken(): String? {
        return authManager.refreshToken
    }

    override suspend fun initAuth(phone: String): Long? {
        return authManager.initSms(phone)?.timer
    }

    override suspend fun loginSms(code: String, phone: String): AuthEntity? {
        return authManager.loginSms(code, phone)?.let { mapper.mapToEntity(it) }
    }

    override suspend fun loginPassword(email: String, password: String): AuthEntity? {
        return authManager.loginPassword(email, password)?.let { mapper.mapToEntity(it) }
    }

    override suspend fun refresh(): AuthEntity? {
        return authManager.refresh()?.let { mapper.mapToEntity(it) }
    }

    override suspend fun logout(): String? {
        return authManager.logout()
    }
}