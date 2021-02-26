package com.foodtechlab.auth.domain.usecase

import com.foodtechlab.auth.domain.port.AuthRepository

/**
 * Created by Umalt on 2/25/21
 */
class InitAuthUseCase(private val repository: AuthRepository) {
    suspend fun execute(phone: String): Long? {
        return repository.initAuth(phone)
    }
}