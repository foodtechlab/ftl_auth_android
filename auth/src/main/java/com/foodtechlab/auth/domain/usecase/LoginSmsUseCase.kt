package com.foodtechlab.auth.domain.usecase

import com.foodtechlab.auth.domain.entity.AuthEntity
import com.foodtechlab.auth.domain.port.AuthRepository

/**
 * Created by Umalt on 2/25/21
 */
class LoginSmsUseCase(private val repository: AuthRepository) {
    suspend fun execute(code: String, phone: String): AuthEntity? {
        return repository.loginSms(code, phone)
    }
}