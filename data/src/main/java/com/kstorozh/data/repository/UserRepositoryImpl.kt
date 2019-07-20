package com.kstorozh.data.repository

import LOG_TAG
import android.util.Log
import com.kstorozh.data.models.ApiResult
import com.kstorozh.data.network.Endpoints
import com.kstorozh.data.network.RemoteData
import com.kstorozh.data.utils.createError
import com.kstorozh.dataimpl.MyError
import com.kstorozh.dataimpl.model.UserLoginParam
import com.kstorozh.dataimpl.model.out.SlackUser
import org.koin.core.KoinComponent

internal class UserRepositoryImpl(
    private val remoteData: RemoteData,
    private val mapper: UserDataMapper,
    private val users: ArrayList<SlackUser>
) : UserRepository, KoinComponent {

    private var myError: MyError? = null

    override suspend fun login(userLoginParam: UserLoginParam): String? {
        Log.d(LOG_TAG, userLoginParam.toString())
        return when (val result = remoteData.login(mapper.mapUserLoginParam(userLoginParam))) {
            is ApiResult.Success -> {
                result.data.data.userId.toString()
            }
            is ApiResult.Error<*> -> {
                myError = createError(Endpoints.LOGIN, result, this)
                null
            }
        }
    }

    override suspend fun getErrors(): MyError? {
        return myError
    }
    override suspend fun getUsers(): List<SlackUser> {
        when (val result = remoteData.getUsers()) {
            is ApiResult.Success -> {
                users.addAll(mapper.mapSlackUserList(result.data.usersData.users))
            }
            is ApiResult.Error<*> -> {
                myError = createError(Endpoints.GET_USERS, result, this)
            }
        }
        return users
    }

    override suspend fun remindPin(slackUserId: String): Boolean {

        return when (val result = remoteData.remindPin(slackUserId)) {
            is ApiResult.Success -> {
                true
            }
            is ApiResult.Error<*> -> {
                myError = createError(Endpoints.REMIND_PIN, result, this)
                false
            }
        }
    }
}