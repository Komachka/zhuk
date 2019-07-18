package com.kstorozh.data.repository

import androidx.lifecycle.MutableLiveData
import com.kstorozh.data.models.ApiResult
import com.kstorozh.data.network.Endpoints
import com.kstorozh.data.network.RemoteData
import com.kstorozh.data.utils.createError
import com.kstorozh.data.utils.getError
import com.kstorozh.dataimpl.MyError
import com.kstorozh.dataimpl.model.UserLoginParam
import com.kstorozh.dataimpl.model.out.SlackUser
import org.koin.core.KoinComponent

internal class UserRepositoryImpl(
    private val remoteData: RemoteData,
    private val mapper: UserDataMapper,
    private val myErrors: MutableLiveData<MyError>,
    private val users: MutableLiveData<List<SlackUser>>
) : UserRepository, KoinComponent {

    override suspend fun login(userLoginParam: UserLoginParam): MutableLiveData<String?> {
        val mutableLiveData = MutableLiveData<String?>()
        when (val result = remoteData.login(mapper.mapUserLoginParam(userLoginParam))) {
            is ApiResult.Success -> {
                mutableLiveData.postValue(result.data.data.userId.toString())
            }
            is ApiResult.Error<*> -> {
                mutableLiveData.postValue(null)
                myErrors.postValue(createError(Endpoints.LOGIN, result, this))
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
                myErrors.postValue(createError(Endpoints.GET_USERS, result, this))
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
                myErrors.postValue(createError(Endpoints.REMIND_PIN, result, this))

            }
        }
        return mutableLiveData
    }
}