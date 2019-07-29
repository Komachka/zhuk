package com.kstorozh.data.utils

import ERROR_STATUS_CODE
import LOG_TAG
import NOT_FOUND_STATUS_CODE
import UNAUTHORIZED_STATUS_CODE
import android.util.Log

import com.kstorozh.data.models.ApiResult
import com.kstorozh.data.network.Endpoints
import com.kstorozh.dataimpl.ErrorStatus
import com.kstorozh.dataimpl.MyError
import retrofit2.Response
import java.lang.Exception

internal fun Response<*>.getErrorStatus(endpoint: Endpoints): ErrorStatus {
    Log.d(LOG_TAG, endpoint.toString())
    return when (endpoint) {
        Endpoints.INIT_DEVICE -> {
            when (code()) {
                ERROR_STATUS_CODE -> ErrorStatus.CAN_NOT_INI_DEVICE
                UNAUTHORIZED_STATUS_CODE -> ErrorStatus.UNAUTHORIZED
                NOT_FOUND_STATUS_CODE -> ErrorStatus.CAN_NOT_INI_DEVICE
                else -> ErrorStatus.UNEXPECTED_ERROR
            }
        }
        Endpoints.UPDATE_DEVICE -> {
            when (code()) {
                ERROR_STATUS_CODE -> ErrorStatus.CAN_NOT_UPDATE_DEVICE
                UNAUTHORIZED_STATUS_CODE -> ErrorStatus.UNAUTHORIZED
                NOT_FOUND_STATUS_CODE -> ErrorStatus.CAN_NOT_UPDATE_DEVICE
                else -> ErrorStatus.UNEXPECTED_ERROR
            }
        }
        Endpoints.TAKE_DEVICE -> {
            when (code()) {
                ERROR_STATUS_CODE -> ErrorStatus.CAN_NOT_BOOK_DEVICE
                UNAUTHORIZED_STATUS_CODE -> ErrorStatus.UNAUTHORIZED
                NOT_FOUND_STATUS_CODE -> ErrorStatus.CAN_NOT_BOOK_DEVICE
                else -> ErrorStatus.UNEXPECTED_ERROR
            }
        }
        Endpoints.RETURN_DEVICE -> {
            when (code()) {
                ERROR_STATUS_CODE -> ErrorStatus.CAN_NOT_RETURN_DEVICE
                UNAUTHORIZED_STATUS_CODE -> ErrorStatus.UNAUTHORIZED
                NOT_FOUND_STATUS_CODE -> ErrorStatus.CAN_NOT_RETURN_DEVICE
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
                NOT_FOUND_STATUS_CODE -> ErrorStatus.CAN_NOT_GET_USERS
                else -> ErrorStatus.UNEXPECTED_ERROR
            }
        }
        Endpoints.REMIND_PIN ->
            when (code()) {
                ERROR_STATUS_CODE -> ErrorStatus.CAN_NOT_REMIND_PIN
                UNAUTHORIZED_STATUS_CODE -> ErrorStatus.UNAUTHORIZED
                NOT_FOUND_STATUS_CODE -> ErrorStatus.CAN_NOT_REMIND_PIN
                else -> ErrorStatus.UNEXPECTED_ERROR
            }
    }
}

internal fun getError(errorStatus: ErrorStatus,exception: Exception): MyError {

    return MyError(errorStatus, exception)
}


internal fun createError(endpoints: Endpoints, result: ApiResult.Error<*>): MyError {
    Log.d(LOG_TAG, result.errorResponse!!.errorBody()!!.string())

    val errorStatus = result.errorResponse!!.getErrorStatus(endpoints)
    val exception = result.exception
    return getError(errorStatus, exception)
}
