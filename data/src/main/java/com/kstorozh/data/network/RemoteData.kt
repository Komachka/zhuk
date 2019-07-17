package com.kstorozh.data.network

import androidx.annotation.WorkerThread
import com.kstorozh.data.models.*
import com.kstorozh.data.models.ApiResult
import com.kstorozh.data.models.BookingBody
import com.kstorozh.data.models.Device
import com.kstorozh.data.models.InitDeviceResponse
import com.kstorozh.data.models.User
import com.kstorozh.data.models.UsersDataResponse

internal interface RemoteData {

    @WorkerThread
    suspend fun initDevice(device: Device): ApiResult<InitDeviceResponse>

    @WorkerThread
    suspend fun updateDevice(device: Device, deviceId: String): ApiResult<BaseResponse>

    @WorkerThread
    suspend fun takeDevise(bookingBody: BookingBody, deviceId: String): ApiResult<BaseResponse>

    @WorkerThread
    suspend fun returnDevice(deviceId: String): ApiResult<BaseResponse>

    @WorkerThread
    suspend fun getUsers(): ApiResult<UsersDataResponse>

    @WorkerThread
    suspend fun createUser(user: User): ApiResult<BaseResponse>

    @WorkerThread
    suspend fun remindPin(slackId: String): ApiResult<BaseResponse>
}