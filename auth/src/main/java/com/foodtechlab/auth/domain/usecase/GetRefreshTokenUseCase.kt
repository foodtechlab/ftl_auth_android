package com.foodtechlab.auth.domain.usecase

import com.foodtechlab.auth.domain.port.AuthRepository

/**
 * Created by Umalt on 2/25/21
 */
class GetRefreshTokenUseCase(private val repository: AuthRepository) {
    fun execute(): String? {
        return repository.getRefreshToken()
    }
}