package com.foodtechlab.auth.di

import com.foodtechlab.auth.AuthManager
import com.foodtechlab.auth.data.mapper.AuthEntityMapper
import com.foodtechlab.auth.data.port.AuthRepositoryImpl
import com.foodtechlab.auth.domain.usecase.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Umalt on 2/26/21
 */
@Module
object AuthModule {
    @Provides
    @Singleton
    fun provideAuthEntityMapper(): AuthEntityMapper {
        return AuthEntityMapper()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        authManager: AuthManager,
        mapper: AuthEntityMapper
    ): AuthRepositoryImpl {
        return AuthRepositoryImpl(authManager, mapper)
    }

    @Provides
    @Singleton
    fun provideInitAuthUseCase(repository: AuthRepositoryImpl): InitAuthUseCase {
        return InitAuthUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCheckAuthUseCase(repository: AuthRepositoryImpl): CheckAuthUseCase {
        return CheckAuthUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideLoginPasswordUseCase(repository: AuthRepositoryImpl): LoginPasswordUseCase {
        return LoginPasswordUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideLoginSmsUseCase(repository: AuthRepositoryImpl): LoginSmsUseCase {
        return LoginSmsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(repository: AuthRepositoryImpl): LogoutUseCase {
        return LogoutUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRefreshUseCase(repository: AuthRepositoryImpl): RefreshUseCase {
        return RefreshUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSaveAccessTokenUseCase(repository: AuthRepositoryImpl): SaveAccessTokenUseCase {
        return SaveAccessTokenUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSaveRefreshTokenUseCase(repository: AuthRepositoryImpl): SaveRefreshTokenUseCase {
        return SaveRefreshTokenUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetAccessTokenUseCase(repository: AuthRepositoryImpl): GetAccessTokenUseCase {
        return GetAccessTokenUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetRefreshTokenUseCase(repository: AuthRepositoryImpl): GetRefreshTokenUseCase {
        return GetRefreshTokenUseCase(repository)
    }
}