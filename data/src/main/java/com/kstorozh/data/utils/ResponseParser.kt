package com.kstorozh.data.utils

import ERROR_STATUS_CODE
import NOT_FOUND_STATUS_CODE
import UNAUTHORIZED_STATUS_CODE
import com.kstorozh.data.models.ApiErrorWithField
import com.kstorozh.data.models.ApiErrorBodyWithMessage
import com.kstorozh.data.network.Endpoints
import com.kstorozh.dataimpl.ErrorStatus
import com.kstorozh.dataimpl.MyError
import okhttp3.ResponseBody
import retrofit2.Response
import java.lang.Exception

internal fun Response<*>.parse(endpoint: Endpoints, exception: Exception): MyError {

    return when (endpoint) {
        Endpoints.INIT_DEVICE ->
        {
            when (code()) {
                ERROR_STATUS_CODE -> MyError(ErrorStatus.CAN_NOT_INI_DEVICE, getErrorMessage(), exception)
                UNAUTHORIZED_STATUS_CODE -> MyError(ErrorStatus.UNAUTHORIZED, getErrorMessage(), exception)
                else -> MyError(ErrorStatus.UNEXPECTED_ERROR, "Exception occurred during init ", exception)
            }
        }
        Endpoints.UPDATE_DEVICE ->
        {
            when (code()) {
                ERROR_STATUS_CODE -> MyError(ErrorStatus.CAN_NOT_UPDATE_DEVICE, getErrorMessage(), exception)
                UNAUTHORIZED_STATUS_CODE -> MyError(ErrorStatus.UNAUTHORIZED, getErrorMessage(), exception)
                else -> MyError(ErrorStatus.UNEXPECTED_ERROR, "Exception occurred during update ", exception)
            }
        }
        Endpoints.TAKE_DEVICE ->
        {
            when (code()) {
                ERROR_STATUS_CODE -> MyError(ErrorStatus.CAN_NOT_BOOK_DEVICE, getErrorMessage(), exception)
                UNAUTHORIZED_STATUS_CODE -> MyError(ErrorStatus.UNAUTHORIZED, getErrorMessage(), exception)
                else -> MyError(ErrorStatus.UNEXPECTED_ERROR, "Exception occurred during booking ", exception)
            }
        }
        Endpoints.RETURN_DEVICE ->
        {
            when (code()) {
                ERROR_STATUS_CODE -> MyError(ErrorStatus.CAN_NOT_RETURN_DEVICE, getErrorMessage(), exception)
                UNAUTHORIZED_STATUS_CODE -> MyError(ErrorStatus.UNAUTHORIZED, getErrorMessage(), exception)
                else -> MyError(ErrorStatus.UNEXPECTED_ERROR, "Exception occurred during booking ", exception)
            }
        }
        Endpoints.LOGIN ->
            when (code()) {
                ERROR_STATUS_CODE -> MyError(ErrorStatus.INVALID_PASSWORD, getErrorMessage(), exception)
                NOT_FOUND_STATUS_CODE -> MyError(ErrorStatus.INVALID_LOGIN, getErrorMessage(), exception)
                UNAUTHORIZED_STATUS_CODE -> MyError(ErrorStatus.UNAUTHORIZED, getErrorMessage(), exception)
                else -> MyError(ErrorStatus.UNEXPECTED_ERROR, "Exception occurred during login ", exception)
            }
        Endpoints.GET_USERS ->
        {
            when (code()) {
                ERROR_STATUS_CODE -> MyError(ErrorStatus.CAN_NOT_GET_USERS, getErrorMessage(), exception)
                UNAUTHORIZED_STATUS_CODE -> MyError(ErrorStatus.UNAUTHORIZED, getErrorMessage(), exception)
                else -> MyError(ErrorStatus.UNEXPECTED_ERROR, "Exception occurred during getting users ", exception)
            }
        }
        Endpoints.REMIND_PIN ->
            when (code()) {
                ERROR_STATUS_CODE -> MyError(ErrorStatus.CAN_NOT_REMIND_PIN, getErrorMessage(), exception)
                UNAUTHORIZED_STATUS_CODE -> MyError(ErrorStatus.UNAUTHORIZED, getErrorMessage(), exception)
                else -> MyError(ErrorStatus.UNEXPECTED_ERROR, "Exception occurred during reminding pin ", exception)
            }
    }
}

private fun Response<*>.getErrorMessage(): String {
    val errorMessage: String
    val retrofit = MyRetrofit.create(AuthInterceptor(TokenRepository()))
        ?: return "Unexpected error. Retrofit == null"
    when (code()) {
        ERROR_STATUS_CODE -> {
            val converter = retrofit
                .responseBodyConverter<ApiErrorWithField>(ApiErrorWithField::class.java, arrayOfNulls<Annotation>(0))
            val body: ResponseBody = body() as ResponseBody
            val error = converter.convert(body)!!
            errorMessage = error.errors.fieldName
        }
        UNAUTHORIZED_STATUS_CODE -> {
            val converter = retrofit
                .responseBodyConverter<ApiErrorBodyWithMessage>(
                    ApiErrorBodyWithMessage::class.java,
                    arrayOfNulls<Annotation>(0)
                )
            val body: ResponseBody = body() as ResponseBody
            val error = converter.convert(body)!!
            errorMessage = error.msg
        }
        else -> errorMessage = "Unexpected error. Status code ${code()}"
    }
    return errorMessage
}
