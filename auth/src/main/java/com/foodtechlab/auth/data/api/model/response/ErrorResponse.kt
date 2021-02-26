package com.foodtechlab.auth.data.api.model.response

/**
 * Created by Umalt on 1/14/21
 */
data class ErrorResponse(
    val domain: String?,
    val details: String?,
    val presentationData: PresentationData?
)

data class PresentationData(
    val title: String?,
    val message: String?
)