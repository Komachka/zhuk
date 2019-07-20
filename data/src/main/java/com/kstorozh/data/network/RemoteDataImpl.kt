package com.kstorozh.data.network

import LOG_TAG
import android.util.Log
import com.kstorozh.data.models.*
import com.kstorozh.data.models.BookingBody
import com.kstorozh.data.models.Device
import com.kstorozh.data.models.ReturnDeviceBody
import com.kstorozh.data.models.User
import retrofit2.Response
import java.io.IOException
import java.lang.Exception

internal class RemoteDataImpl(
    private val deviceApi: DeviceApi,
    private val userApi: UserApi
) : RemoteData {
    override suspend fun login(userLoginParam: UserLogin): ApiResult<LoginUserResponce> {
        val errorMessage = "problem with login"
        Log.d(LOG_TAG, userLoginParam.toString())
        return getApiResult(errorMessage) {
            userApi.login(userLoginParam)
        }
    }

    private suspend fun <T : Any> getApiResult(errorMessage: String, call: suspend () -> Response<T>): ApiResult<T> {
        try {
            val response = call.invoke()
            if (response.isSuccessful) return ApiResult.Success(response.body()!!)
            else return ApiResult.Error(IOException("Api error $errorMessage"), response)
        } catch (ex: Exception) {
            return ApiResult.Error<Exception>(ex)
        }
    }

    override suspend fun initDevice(device: Device): ApiResult<InitDeviceResponse> {
        val errorMessage = "Problem with init device"
        return getApiResult(errorMessage) {
            deviceApi.initDevice(device)
        }
    }

    override suspend fun updateDevice(device: Device, deviceId: String): ApiResult<BaseResponse> {
        val errorMessage = "problem with update device"
        return getApiResult(errorMessage) {
            deviceApi.updateDevice(device, deviceId)
        }
    }

    override suspend fun takeDevise(bookingBody: BookingBody, deviceId: String): ApiResult<BaseResponse> {
        val errorMessage = "problem with update device"
        return getApiResult(errorMessage) {
            deviceApi.takeDevice(bookingBody = bookingBody)
        }
    }

    override suspend fun returnDevice(returnDeviceBody: ReturnDeviceBody): ApiResult<BaseResponse> {
        val errorMessage = "problem with update device"
        return getApiResult(errorMessage) {
            deviceApi.returnDevice(returnDeviceBody, returnDeviceBody.deviceId.toString())
        }
    }

    override suspend fun getUsers(): ApiResult<UsersDataResponse> {
        val errorMessage = "problem with getting list of users"
        return getApiResult(errorMessage) {
            userApi.getUsers()
        }
    }

    override suspend fun createUser(user: User): ApiResult<BaseResponse> {
        val errorMessage = "problem with creating user"
        return getApiResult(errorMessage) {
            userApi.createUser(user = user)
        }
    }

    override suspend fun remindPin(slackUserId: String): ApiResult<BaseResponse> {
        val errorMessage = "problem with reminding pin"
        return getApiResult(errorMessage) {
            userApi.remindPin(userId = slackUserId)
        }
    }
}