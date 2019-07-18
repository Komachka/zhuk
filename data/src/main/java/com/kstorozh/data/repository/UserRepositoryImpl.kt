package com.kstorozh.data.repository

import androidx.lifecycle.MutableLiveData
import com.kstorozh.data.models.ApiResult
import com.kstorozh.data.network.Endpoints
import com.kstorozh.data.network.RemoteData
import com.kstorozh.data.utils.parse
import com.kstorozh.dataimpl.MyError
import com.kstorozh.dataimpl.model.UserLoginParam
import com.kstorozh.dataimpl.model.out.SlackUser

internal class UserRepositoryImpl(
    private val remoteData: RemoteData,
    private val mapper: UserDataMapper,
    private val myErrors: MutableLiveData<MyError>,
    private val users: MutableLiveData<List<SlackUser>>
) : UserRepository {

    override suspend fun login(userLoginParam: UserLoginParam): MutableLiveData<String?> {
        val mutableLiveData = MutableLiveData<String?>()
        when (val result = remoteData.login(mapper.mapUserLoginParam(userLoginParam))) {
            is ApiResult.Success -> {
                mutableLiveData.postValue(result.data.data.userId.toString())
            }
            is ApiResult.Error<*> -> {
                mutableLiveData.postValue(null)
                myErrors.postValue(result.errorResponse.parse(Endpoints.LOGIN, result.exception))
            }
        }
        return mutableLiveData
    }

    override suspend fun getErrors(): MutableLiveData<MyError> {
        return myErrors
    }
    override suspend fun getUsers(): MutableLiveData<List<SlackUser>> {
        when (val result = remoteData.getUsers()) {
            is ApiResult.Success -> {
                users.postValue(mapper.mapSlackUserList(result.data.usersData.users))
            }
            is ApiResult.Error<*> -> {
                myErrors.postValue(result.errorResponse.parse(Endpoints.GET_USERS, result.exception))
            }
        }
        return users
    }

    override suspend fun remindPin(slackUserId: String): MutableLiveData<Boolean> {

        val mutableLiveData = MutableLiveData<Boolean>()
        when (val result = remoteData.remindPin(slackUserId)) {
            is ApiResult.Success -> {
                mutableLiveData.postValue(true)
            }
            is ApiResult.Error<*> -> {
                mutableLiveData.postValue(false)
                myErrors.postValue(result.errorResponse.parse(Endpoints.REMIND_PIN, result.exception))
            }
        }
        return mutableLiveData
    }
}