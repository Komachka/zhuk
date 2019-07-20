package com.kstorozh.data.utils

import ERROR_STATUS_CODE
import NOT_FOUND_STATUS_CODE
import UNAUTHORIZED_STATUS_CODE
import com.kstorozh.data.models.ApiErrorWithField
import com.kstorozh.data.models.ApiErrorBodyWithMessage
import com.kstorozh.data.models.ApiResult
import com.kstorozh.data.network.Endpoints
import com.kstorozh.dataimpl.ErrorStatus
import com.kstorozh.dataimpl.MyError
import okhttp3.ResponseBody
import org.koin.core.KoinComponent
import org.koin.core.get
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.Exception

internal fun Response<*>.getErrorStatus(endpoint: Endpoints): ErrorStatus {
    return when (endpoint) {
        Endpoints.INIT_DEVICE -> {
            when (code()) {
                ERROR_STATUS_CODE -> ErrorStatus.CAN_NOT_INI_DEVICE
                UNAUTHORIZED_STATUS_CODE -> ErrorStatus.UNAUTHORIZED
                else -> ErrorStatus.UNEXPECTED_ERROR
            }
        }
        Endpoints.UPDATE_DEVICE -> {
            when (code()) {
                ERROR_STATUS_CODE -> ErrorStatus.CAN_NOT_UPDATE_DEVICE
                UNAUTHORIZED_STATUS_CODE -> ErrorStatus.UNAUTHORIZED
                else -> ErrorStatus.UNEXPECTED_ERROR
            }
        }
        Endpoints.TAKE_DEVICE -> {
            when (code()) {
                ERROR_STATUS_CODE -> ErrorStatus.CAN_NOT_BOOK_DEVICE
                UNAUTHORIZED_STATUS_CODE -> ErrorStatus.UNAUTHORIZED
                else -> ErrorStatus.UNEXPECTED_ERROR
            }
        }
        Endpoints.RETURN_DEVICE -> {
            when (code()) {
                ERROR_STATUS_CODE -> ErrorStatus.CAN_NOT_RETURN_DEVICE
                UNAUTHORIZED_STATUS_CODE -> ErrorStatus.UNAUTHORIZED
                else -> ErrorStatus.UNEXPECTED_ERROR
            }
        }
        Endpoints.LOGIN ->
            when (code()) {
                ERROR_STATUS_CODE -> ErrorStatus.INVALID_PASSWORD
                NOT_FOUND_STATUS_CODE -> ErrorStatus.INVALID_LOGIN
                UNAUTHORIZED_STATUS_CODE -> ErrorStatus.UNAUTHORIZED
                else -> ErrorStatus.UNEXPECTED_ERROR
            }
        Endpoints.GET_USERS -> {
            when (code()) {
                ERROR_STATUS_CODE -> ErrorStatus.CAN_NOT_GET_USERS
                UNAUTHORIZED_STATUS_CODE -> ErrorStatus.UNAUTHORIZED
                else -> ErrorStatus.UNEXPECTED_ERROR
            }
        }
        Endpoints.REMIND_PIN ->
            when (code()) {
                ERROR_STATUS_CODE -> ErrorStatus.CAN_NOT_REMIND_PIN
                UNAUTHORIZED_STATUS_CODE -> ErrorStatus.UNAUTHORIZED
                else -> ErrorStatus.UNEXPECTED_ERROR
            }
    }
}

internal fun getError(errorStatus: ErrorStatus, message: String, exception: Exception): MyError {

    return MyError(errorStatus, message, exception)
}

internal fun Response<*>.parseErrorMessage(koin: KoinComponent): String {
    var errorMessage = "Unexpected error. Status code ${code()}"
    val retrofit: Retrofit = koin.get()
    when (code()) {
        ERROR_STATUS_CODE -> {
            val converter = retrofit
                .responseBodyConverter<ApiErrorWithField>(ApiErrorWithField::class.java, arrayOfNulls<Annotation>(0))

            body()?.let {
                val body: ResponseBody = body() as ResponseBody
                val error = converter.convert(body)!!
                errorMessage = error.errors.fieldName
            }
            errorBody().let {
                val body: ResponseBody = errorBody() as ResponseBody
                val error = converter.convert(body)!!
                errorMessage = error.errors.fieldName
            }
        }
        UNAUTHORIZED_STATUS_CODE -> {
            val converter = retrofit
                .responseBodyConverter<ApiErrorBodyWithMessage>(
                    ApiErrorBodyWithMessage::class.java,
                    arrayOfNulls<Annotation>(0)
                )

            body()?.let {
                val body: ResponseBody = body() as ResponseBody
                val error = converter.convert(body)!!
                errorMessage = error.msg
            }
            errorBody()?.let {
                val body: ResponseBody = errorBody() as ResponseBody
                val error = converter.convert(body)!!
                errorMessage = error.msg
            }
        }
    }
    return errorMessage
}

internal fun createError(endpoints: Endpoints, result: ApiResult.Error<*>, component: KoinComponent): MyError {
    val errorStatus = result.errorResponse?.getErrorStatus(endpoints) ?: ErrorStatus.UNEXPECTED_ERROR
    val message = result.errorResponse?.parseErrorMessage(component) ?: "Undefine"
    val exception = result.exception
    return getError(errorStatus, message, exception)
}
