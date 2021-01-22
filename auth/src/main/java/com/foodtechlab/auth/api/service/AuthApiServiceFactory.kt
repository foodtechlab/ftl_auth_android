package com.foodtechlab.auth.api.service

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Umalt on 1/14/21
 */
object AuthApiServiceFactory {

    fun makeAuthApiService(
        baseUrl: String,
        apiInterceptor: Interceptor?,
        httpLoggingInterceptor: Interceptor?
    ): AuthApiService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(makeOkHttpClient(apiInterceptor, httpLoggingInterceptor))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }

    private fun makeOkHttpClient(
        apiInterceptor: Interceptor?,
        httpLoggingInterceptor: Interceptor?
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .apply {
                apiInterceptor?.let { addInterceptor(it) }
                httpLoggingInterceptor?.let { addInterceptor(it) }
            }
            .build()
    }
}