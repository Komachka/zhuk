package com.kstorozh.data.utils

import ERROR_STATUS_CODE
import UNAUTHORIZED_STATUS_CODE
import com.kstorozh.data.models.ApiErrorBody
import com.kstorozh.data.models.ApiErrorBodyUnexpected
import com.kstorozh.data.models.ApiErrorWithField
import com.kstorozh.data.models.ApiErrorBodyWithMessage
import com.kstorozh.data.network.Endpoints
import com.kstorozh.dataimpl.ErrorStatus
import com.kstorozh.dataimpl.MyError
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.Exception

internal fun Response<*>.parse(endpoint:Endpoints, exception: Exception): MyError {

    val error: MyError
    val retrofit = MyRetrofit.create(AuthInterceptor(TokenRepository()))
        ?: return MyError(ErrorStatus.UNEXPECTED_ERROR,"Unexpected error. Retrofit == null",exception)

    error = when(endpoint)
    {
        Endpoints.INIT_DEVICE ->
        {
            when(code())
            {
                ERROR_STATUS_CODE -> MyError(ErrorStatus.CAN_NOT_INI_DEVICE, getErrorMessage(retrofit), exception)
            }
        }
        Endpoints.UPDATE_DEVICE ->
        Endpoints.TAKE_DEVICE ->
        Endpoints.RETURN_DEVICE ->
        Endpoints.LOGIN ->
        Endpoints.GET_USERS ->
        Endpoints.REMIND_PIN ->

    }

}


private fun Response<*>.getErrorMessage(retrofit:Retrofit) : String
{
    val errorMessage : String
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
