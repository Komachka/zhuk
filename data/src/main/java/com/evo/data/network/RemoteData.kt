package com.evo.data.network

import androidx.annotation.WorkerThread
import com.evo.data.models.*
import com.evo.data.models.ApiResult
import com.evo.data.models.BookingBody
import com.evo.data.models.Device
import com.evo.data.models.InitDeviceResponse

internal interface RemoteData {

    @WorkerThread
    suspend fun initDevice(device: Device): ApiResult<InitDeviceResponse>

    @WorkerThread
    suspend fun updateDevice(device: Device, deviceId: String): ApiResult<BaseResponse>

    @WorkerThread
    suspend fun takeDevise(bookingBody: BookingBody, deviceId: String): ApiResult<BookingCreated>

    @WorkerThread
    suspend fun returnDevice(returnDeviceBody: ReturnDeviceBody, bookingId: Int): ApiResult<BaseResponse>

    @WorkerThread
    suspend fun getUsers(): ApiResult<UsersData>

    @WorkerThread

    suspend fun remindPin(slackId: String): ApiResult<BaseResponse>

    @WorkerThread
    suspend fun login(userLoginParam: UserLogin): ApiResult<LoginUserResponse>

    @WorkerThread
    suspend fun getBookingByDate(startDate: String, endDate: String): ApiResult<BookingDataByDay>

    @WorkerThread
    suspend fun deleteBooking(returnBookingBody: DeleteBookingBody, bookingId: Int): ApiResult<BaseResponse>

    @WorkerThread
    suspend fun editBooking(bookingBody: BookingBody, bookingId: String): ApiResult<BaseResponse>

    @WorkerThread
    suspend fun getNearbyBooking(id: String): ApiResult<NewarBookingResult>

    @WorkerThread
    suspend fun sendRepo(report: Report): ApiResult<BaseResponse>
}
