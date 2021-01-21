package com.foodtechlab.auth.api.service

import com.foodtechlab.auth.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Umalt on 1/14/21
 */
object AuthApiServiceFactory {

    fun makeAuthApiService(
        baseUrl: String,
        apiInterceptor: Interceptor?
    ): AuthApiService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(makeOkHttpClient(apiInterceptor))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }

    private fun makeOkHttpClient(apiInterceptor: Interceptor?): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(makeHttpLoggingInterceptor())
            .apply { apiInterceptor?.let { addInterceptor(it) } }
            .build()
    }

    private fun makeHttpLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }
}