package com.kstorozh.data.utils

import ERROR_STATUS_CODE
import UNAUTHORIZED_STATUS_CODE
import com.kstorozh.data.models.ApiErrorBody
import com.kstorozh.data.models.ApiErrorBodyUnexpected
import com.kstorozh.data.models.ApiErrorWithField
import com.kstorozh.data.models.ApiErrorBodyWithMessage
import okhttp3.ResponseBody
import retrofit2.Response

internal fun Response<*>.parse(): ApiErrorBody {

    val error: ApiErrorBody
    val retrofit = MyRetrofit.create()
    when (code()) {
            ERROR_STATUS_CODE -> {
                val converter = retrofit
                    .responseBodyConverter<ApiErrorWithField>(ApiErrorWithField::class.java, arrayOfNulls<Annotation>(0))
                val body: ResponseBody = body() as ResponseBody
                error = converter.convert(body)!!
            }
            UNAUTHORIZED_STATUS_CODE -> {
                val converter = retrofit
                    .responseBodyConverter<ApiErrorBodyWithMessage>(ApiErrorBodyWithMessage::class.java, arrayOfNulls<Annotation>(0))
                val body: ResponseBody = body() as ResponseBody
                error = converter.convert(body)!!
            }
            else -> error = ApiErrorBodyUnexpected("Unexpected error. Status code ${code()}")
        }
    return error
}
