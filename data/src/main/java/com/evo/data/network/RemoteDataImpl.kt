package com.evo.data.network

import BOOKING_CALENDAR_ERROR
import BOOKING_EDIT_ERROR
import BOOKING_ERROR
import DELETE_BOOKING_ERROR
import GET_NEARBY_BOOKING_ERROR
import GET_USERS_ERROR
import INIT_ERROR
import LOGIN_ERROR
import REMIND_PIN_ERROR
import RETURN_ERROR
import SEND_REPORT_ERROR
import UPDATE_ERROR
import com.evo.data.models.*
import com.evo.data.models.BookingBody
import com.evo.data.models.Device
import com.evo.data.models.ReturnDeviceBody
import retrofit2.Response
import java.io.IOException
import java.lang.Exception

internal class RemoteDataImpl(
    private val deviceApi: DeviceApi,
    private val userApi: UserApi,
    private val calendarApi: CalendarApi
) : RemoteData {
    override suspend fun sendRepo(report: Report): ApiResult<BaseResponse> {
        return getApiResult(SEND_REPORT_ERROR) {
            deviceApi.sendReport(report)
        }
    }

    override suspend fun getNearbyBooking(id: String): ApiResult<NewarBookingResult> {
        return getApiResult(GET_NEARBY_BOOKING_ERROR) {
            calendarApi.getNearBooking(DeviceId(id.toInt()))
        }
    }

    override suspend fun editBooking(bookingBody: BookingBody, bookingId: String): ApiResult<BaseResponse> {
        return getApiResult(BOOKING_EDIT_ERROR) {
            deviceApi.editBooking(bookingBody, bookingId.toInt())
        }
    }

    override suspend fun deleteBooking(
        deleteBookingBody: DeleteBookingBody,
        bookingId: Int
    ): ApiResult<BaseResponse> {
        return getApiResult(DELETE_BOOKING_ERROR) {
            calendarApi.deleteBooking(deleteBookingBody, bookingId)
        }
    }

    override suspend fun getBookingByDate(startDate: String, endDate: String): ApiResult<BookingDataByDay> {
        return getApiResult(BOOKING_CALENDAR_ERROR) {
            calendarApi.getBookingByDate(startDate, endDate)
        }
    }

    override suspend fun login(userLoginParam: UserLogin): ApiResult<LoginUserResponse> {
        return getApiResult(LOGIN_ERROR) {
            userApi.login(userLoginParam)
        }
    }

    private suspend fun <T : Any> getApiResult(errorMessage: String, call: suspend () -> Response<T>): ApiResult<T> {
        try {
            val response = call.invoke()
            if (response.isSuccessful) return ApiResult.Success(response.body()!!)
            else return ApiResult.Error(IOException(errorMessage), response)
        } catch (ex: Exception) {
            return ApiResult.Error<Exception>(ex)
        }
    }

    override suspend fun initDevice(device: Device): ApiResult<InitDeviceResponse> {
        return getApiResult(INIT_ERROR) {
            deviceApi.initDevice(device)
        }
    }

    override suspend fun updateDevice(device: Device, deviceId: String): ApiResult<BaseResponse> {
        return getApiResult(UPDATE_ERROR) {
            deviceApi.updateDevice(device, deviceId)
        }
    }

    override suspend fun takeDevise(bookingBody: BookingBody, deviceId: String): ApiResult<BookingCreated> {
        return getApiResult(BOOKING_ERROR) {
            deviceApi.takeDevice(bookingBody = bookingBody)
        }
    }

    override suspend fun returnDevice(returnDeviceBody: ReturnDeviceBody, bookingId: Int): ApiResult<BaseResponse> {
        return getApiResult(RETURN_ERROR) {
            deviceApi.returnDevice(returnDeviceBody, bookingId)
        }
    }

    override suspend fun getUsers(): ApiResult<UsersData> {
        return getApiResult(GET_USERS_ERROR) {
            userApi.getUsers()
        }
    }

    override suspend fun remindPin(slackUserId: String): ApiResult<BaseResponse> {
        return getApiResult(REMIND_PIN_ERROR) {
            userApi.remindPin(userId = slackUserId)
        }
    }
}