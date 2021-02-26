package com.foodtechlab.auth.domain.usecase

import com.foodtechlab.auth.domain.entity.AuthEntity
import com.foodtechlab.auth.domain.port.AuthRepository

/**
 * Created by Umalt on 2/25/21
 */
class LoginPasswordUseCase(private val repository: AuthRepository) {
    suspend fun execute(email: String, password: String): AuthEntity? {
        return repository.loginPassword(email, password)
    }
}