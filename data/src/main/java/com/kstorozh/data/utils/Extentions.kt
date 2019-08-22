package com.kstorozh.data.utils

import CONFLICT_STATUS_CODE
import ERROR_STATUS_CODE
import LOG_TAG
import NOT_FOUND_STATUS_CODE
import UNAUTHORIZED_STATUS_CODE
import android.util.Log
import com.kstorozh.data.models.ApiErrorBodyWithError

import com.kstorozh.data.models.ApiResult
import com.kstorozh.data.network.Endpoints
import com.kstorozh.dataimpl.ErrorStatus
import com.kstorozh.dataimpl.DataError
import okhttp3.ResponseBody
import org.koin.core.KoinComponent
import org.koin.core.get
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.Exception

internal fun Response<*>.getErrorStatus(endpoint: Endpoints): ErrorStatus {
    return when (endpoint) {
        Endpoints.INIT_DEVICE -> code().getErrorStatusByCode(ErrorStatus.CAN_NOT_INI_DEVICE, ErrorStatus.UNAUTHORIZED, ErrorStatus.CAN_NOT_INI_DEVICE)

        Endpoints.UPDATE_DEVICE ->
            code().getErrorStatusByCode(ErrorStatus.CAN_NOT_UPDATE_DEVICE, notFound = ErrorStatus.CAN_NOT_UPDATE_DEVICE)

        Endpoints.TAKE_DEVICE ->
            code().getErrorStatusByCode(ErrorStatus.CAN_NOT_BOOK_DEVICE, notFound = ErrorStatus.CAN_NOT_BOOK_DEVICE)

        Endpoints.RETURN_DEVICE ->
            code().getErrorStatusByCode(ErrorStatus.CAN_NOT_RETURN_DEVICE, notFound = ErrorStatus.CAN_NOT_RETURN_DEVICE)

        Endpoints.LOGIN ->
            code().getErrorStatusByCode(ErrorStatus.INVALID_PASSWORD, notFound = ErrorStatus.INVALID_LOGIN)

        Endpoints.GET_USERS ->
            code().getErrorStatusByCode(ErrorStatus.CAN_NOT_GET_USERS, notFound = ErrorStatus.CAN_NOT_GET_USERS)

        Endpoints.REMIND_PIN ->
            code().getErrorStatusByCode(ErrorStatus.CAN_NOT_REMIND_PIN, notFound = ErrorStatus.CAN_NOT_REMIND_PIN)
        Endpoints.GET_BOOKING ->
            code().getErrorStatusByCode(ErrorStatus.CAN_NOT_GET_BOOKING, notFound = ErrorStatus.CAN_NOT_GET_BOOKING)
    }
}

internal fun getError(errorStatus: ErrorStatus?, message: String?, exception: Exception): DataError {
    return DataError(errorStatus, message, exception)
}

internal fun createError(endpoints: Endpoints, result: ApiResult.Error<*>, koinComponent: KoinComponent): DataError {
    val errorStatus = result.errorResponse?.getErrorStatus(endpoints) ?: ErrorStatus.UNEXPECTED_ERROR
    var message: String? = null
    result.errorResponse?.errorBody()?.let {
        try {
            val error = tryConvertError(ApiErrorBodyWithError::class.java, it, koinComponent)
            message = error?.errors
            if (message == null)
                message = error?.msg
        } catch (e: Exception) {
            Log.d(LOG_TAG, "Can not convert error message ${it.string()} because ${e.message}")
            message = null
        }
    }
    return getError(errorStatus, message, result.exception)
}

private fun Int.getErrorStatusByCode(error: ErrorStatus, unauthorised: ErrorStatus = ErrorStatus.UNAUTHORIZED, notFound: ErrorStatus): ErrorStatus {
    return when (this) {
        ERROR_STATUS_CODE -> error
        UNAUTHORIZED_STATUS_CODE -> unauthorised
        NOT_FOUND_STATUS_CODE -> notFound
        CONFLICT_STATUS_CODE -> ErrorStatus.CONFLICT_ERROR
        else -> ErrorStatus.UNEXPECTED_ERROR
    }
}

fun <T> tryConvertError(type: Class<T>, it: ResponseBody, koinComponent: KoinComponent): T? {
    val retrofit: Retrofit = koinComponent.get()
    val errorConverter =
        retrofit.responseBodyConverter<T>(type, arrayOfNulls<Annotation>(0))
    return errorConverter.convert(it)
}
