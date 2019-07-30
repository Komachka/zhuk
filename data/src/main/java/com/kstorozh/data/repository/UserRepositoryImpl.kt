package com.kstorozh.data.repository

import LOG_TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.kstorozh.data.models.ApiResult
import com.kstorozh.data.network.Endpoints
import com.kstorozh.data.network.RemoteData
import com.kstorozh.data.utils.createError
import com.kstorozh.dataimpl.DataError
import com.kstorozh.dataimpl.model.UserLoginParam
import com.kstorozh.dataimpl.model.out.BookingSessionData
import com.kstorozh.dataimpl.model.out.RepoResult
import com.kstorozh.dataimpl.model.out.SlackUser

internal class UserRepositoryImpl(
    private val remoteData: RemoteData,
    private val mapper: UserDataMapper
    //private val users: ArrayList<SlackUser>
) : UserRepository {

    private val myError: MutableLiveData<DataError> = MutableLiveData()

    override suspend fun login(userLoginParam: UserLoginParam): RepoResult<String> {
        val repoResult: RepoResult<String> = RepoResult()
        return when (val result = remoteData.login(mapper.mapUserLoginParam(userLoginParam))) {
            is ApiResult.Success -> {
                repoResult.data = result.data.data.userId.toString()
                repoResult
            }
            is ApiResult.Error<*> -> {
                myError.postValue(createError(Endpoints.LOGIN, result))
                Log.d(LOG_TAG, "My error $myError")
                repoResult.error = createError(Endpoints.LOGIN, result)
                repoResult
            }
        }
    }

    override suspend fun getErrors(): MutableLiveData<DataError> {
        return myError
    }
    override suspend fun getUsers(): RepoResult<List<SlackUser>> {
        val repoResult: RepoResult<List<SlackUser>> = RepoResult()
        val users: ArrayList<SlackUser> = ArrayList()
        when (val result = remoteData.getUsers()) {
            is ApiResult.Success -> {
                users.addAll(mapper.mapSlackUserList(result.data.users))
                repoResult.data = users
            }
            is ApiResult.Error<*> -> {
                myError.postValue(createError(Endpoints.GET_USERS, result))
                repoResult.error = createError(Endpoints.GET_USERS, result)
            }
        }
        return repoResult
    }

    override suspend fun remindPin(slackUserId: String): RepoResult<Boolean> {
        val repoResult: RepoResult<Boolean> = RepoResult()
        return when (val result = remoteData.remindPin(slackUserId)) {
            is ApiResult.Success -> {
                repoResult.data = true
                repoResult
            }
            is ApiResult.Error<*> -> {
                myError.postValue(createError(Endpoints.REMIND_PIN, result))
                repoResult.data = false
                repoResult.error = createError(Endpoints.REMIND_PIN, result)
                repoResult
            }
        }
    }
}