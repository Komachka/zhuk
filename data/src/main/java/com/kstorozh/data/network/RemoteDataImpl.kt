package com.kstorozh.data.network

import android.util.Log
import com.kstorozh.data.models.*
import com.kstorozh.data.models.BookingBody
import com.kstorozh.data.models.Device
import com.kstorozh.data.models.StatusBody
import com.kstorozh.data.models.User
import retrofit2.Response
import java.io.IOException

internal sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

internal class RemoteDataImpl(
    private val deviceApi: DeviceApi,
    private val userApi: UserApi
) : RemoteData {

    private val TAG: String = "RemoteData"
    private suspend fun <T : Any> getApiResult(call: suspend () -> Response<T>, errorMessage: String): Result<T> {
        val response = call.invoke()
        if (response.isSuccessful) return Result.Success(response.body()!!)
        // TODO here we can cast response to error message model deepens on status code an get an error or message
        return Result.Error(IOException("Error Occurred during getting safe Api result, Custom ERROR - $errorMessage"))
    }

    override suspend fun initDevice(device: Device): InitDeviceResponse? {
        val errorMessage = "Problem with init device"
        val result = getApiResult({ deviceApi.initDevice(device) }, errorMessage)
        var data: InitDeviceResponse? = null
        when (result) {
            is Result.Success ->
                data = result.data
            is Result.Error -> {
                Log.d(TAG, "$errorMessage & Exception - ${result.exception}")
            }
        }
        return data
    }

    override suspend fun updateDevice(device: Device, deviceId: String) {
        deviceApi.updateDevice(device, deviceId)
    }

    override suspend fun takeDevise(bookingBody: BookingBody, deviceId: String) {
        deviceApi.takeDevice(bookingBody = bookingBody)
    }

    override suspend fun returnDevice(deviceId: String) {
        deviceApi.returnDevice(status = StatusBody(status = true), deviceId = deviceId)
    }

    override suspend fun getUsers(): List<User>? {
        val errorMessage = "Can not get list of users"
        val result = getApiResult({ userApi.getUsers() }, errorMessage)
        var data: List<User>? = null
        when (result) {
            is Result.Success ->
                data = result.data.usersData.users
            is Result.Error -> {
                Log.d(TAG, "$errorMessage & Exception - ${result.exception}")
            }
        }
        return data
    }

    override suspend fun createUser(user: User) {
        userApi.createUser(user = user)
    }

    override suspend fun remindPin(user: User) {
        userApi.remindPin(userId = user.id.toString())
    }
}