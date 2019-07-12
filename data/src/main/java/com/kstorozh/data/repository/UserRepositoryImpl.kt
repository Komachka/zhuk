package com.kstorozh.data.repository

import androidx.lifecycle.MutableLiveData
import com.kstorozh.data.models.ApiError
import com.kstorozh.data.models.ApiResult
import com.kstorozh.data.models.User
import com.kstorozh.data.network.RemoteData
import com.kstorozh.data.utils.parse
import com.kstorozh.dataimpl.model.UserParam

internal class UserRepositoryImpl(
    private val remoteData: RemoteData,
    private val mapper: UserDataMapper,
    private val apiError: MutableLiveData<ApiError>,
    private val users: MutableLiveData<List<User>>
) : UserRepository {
    override suspend fun getUsers() {
        when (val result = remoteData.getUsers()) {
            is ApiResult.Success -> {
                users.postValue(result.data.usersData.users)
            }
            is ApiResult.Error<*> -> {
                apiError.postValue(ApiError(result.errorResponse.parse(), result.exception))
            }
        }
    }

    override suspend fun createUser(userParam: UserParam) {

        when (val result = remoteData.createUser(mapper.mapUserParam(userParam))) {
            is ApiResult.Success -> {
                // TODO do smth
            }
            is ApiResult.Error<*> -> {
                apiError.postValue(ApiError(result.errorResponse.parse(), result.exception))
            }
        }
    }

    override suspend fun remindPin(userParam: UserParam) {

        when (val result = remoteData.remindPin(mapper.mapUserParam(userParam))) {
            is ApiResult.Success -> {
                // TODO do smth
            }
            is ApiResult.Error<*> -> {
                apiError.postValue(ApiError(result.errorResponse.parse(), result.exception))
            }
        }
    }
}