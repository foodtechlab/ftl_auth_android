package com.foodtechlab.auth.data.api.model.response

/**
 * Created by Umalt on 1/14/21
 */
data class BaseResponse<T : Any?>(
    val result: T,
    val errors: List<ErrorResponse>?
)