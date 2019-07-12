package com.kstorozh.data.models

import retrofit2.Response

internal sealed class ApiResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : ApiResult<T>()
    data class Error<T : Any>(
        val exception: Exception,
        val errorResponse: Response<T>
    ) : ApiResult<Nothing>()
}