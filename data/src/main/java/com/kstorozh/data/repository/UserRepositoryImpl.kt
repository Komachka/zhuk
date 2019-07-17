package com.kstorozh.data.repository

import androidx.lifecycle.MutableLiveData
import com.kstorozh.data.errors.ApiError
import com.kstorozh.data.models.ApiResult
import com.kstorozh.data.network.RemoteData
import com.kstorozh.data.utils.parse
import com.kstorozh.dataimpl.MyErrors
import com.kstorozh.dataimpl.model.into.UserParam
import com.kstorozh.dataimpl.model.out.SlackUser

internal class UserRepositoryImpl(
    private val remoteData: RemoteData,
    private val mapper: UserDataMapper
) : UserRepository {
    private val myError: MutableLiveData<MyErrors> by lazy { MutableLiveData<MyErrors>() }
    private val users: MutableLiveData<List<SlackUser>> by lazy { MutableLiveData<List<SlackUser>>() }

    override suspend fun getErrors(): MutableLiveData<MyErrors> {
        return myError
    }
    override suspend fun getUsers(): MutableLiveData<List<SlackUser>> {
        when (val result = remoteData.getUsers()) {
            is ApiResult.Success -> {
                users.postValue(mapper.mapSlackUserList(result.data.usersData.users))
            }
            is ApiResult.Error<*> -> {
                myError.postValue(ApiError(result.errorResponse.parse(), result.exception))
            }
        }
        return users
    }

    override suspend fun createUser(userParam: UserParam) {

        when (val result = remoteData.createUser(mapper.mapUserParam(userParam))) {
            is ApiResult.Success -> {
                // TODO do smth
            }
            is ApiResult.Error<*> -> {
                myError.postValue(ApiError(result.errorResponse.parse(), result.exception))
            }
        }
    }

    override suspend fun remindPin(slackUserId: String) : Boolean {

        return when (val result = remoteData.remindPin(slackUserId)) {
            is ApiResult.Success -> {
               true
            }
            is ApiResult.Error<*> -> {
                myError.postValue(ApiError(result.errorResponse.parse(), result.exception))
                false
            }
        }
    }
}