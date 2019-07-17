package com.kstorozh.data.repository

import androidx.lifecycle.MutableLiveData
import com.kstorozh.data.errors.ApiError
import com.kstorozh.data.models.ApiErrorBodyUnexpected
import com.kstorozh.data.models.ApiErrorBodyWithMessage
import com.kstorozh.data.models.ApiErrorWithField
import com.kstorozh.data.models.ApiResult
import com.kstorozh.data.network.Endpoints
import com.kstorozh.data.network.RemoteData
import com.kstorozh.data.utils.parse
import com.kstorozh.dataimpl.ErrorStatus
import com.kstorozh.dataimpl.MyError
import com.kstorozh.dataimpl.model.UserLoginParam
import com.kstorozh.dataimpl.model.into.UserParam
import com.kstorozh.dataimpl.model.out.SlackUser

internal class UserRepositoryImpl(
    private val remoteData: RemoteData,
    private val mapper: UserDataMapper
) : UserRepository {

    private val myErrors: MutableLiveData<MyError> by lazy { MutableLiveData() }
    private val users: MutableLiveData<List<SlackUser>> by lazy { MutableLiveData() }

    override suspend fun login(userLoginParam: UserLoginParam): MutableLiveData<String?> {
        val mutableLiveData = MutableLiveData<String?>()
        when (val result = remoteData.login(mapper.mapUserLoginParam(userLoginParam))) {
            is ApiResult.Success -> {
                mutableLiveData.postValue(result.data.data.userId.toString())
            }
            is ApiResult.Error<*> -> {
                mutableLiveData.postValue(null)

                val error = result.errorResponse.parse(Endpoints.LOGIN, result.exception)



/*
                val error = when(val errorBody =  result.errorResponse.parse())
                {
                    is ApiErrorWithField -> MyError(ErrorStatus.INVALID_PASSWORD, errorBody.errors.fieldName, result.exception)
                    is ApiErrorBodyWithMessage -> MyError(ErrorStatus.INVALID_LOGIN, errorBody.msg, result.exception)
                    is ApiErrorBodyUnexpected -> MyError(ErrorStatus.UNEXPECTED_ERROR, errorBody.message, result.exception)
                    else -> MyError(ErrorStatus.UNEXPECTED_ERROR, "something went wrong", result.exception)
                }
*/
                myErrors.postValue(error)
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
                myErrors.postValue(MyError(ApiError(result.errorResponse.parse(), result.exception)))
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
                myErrors.postValue(MyError(ApiError(result.errorResponse.parse(), result.exception)))
            }
        }
    }

    override suspend fun remindPin(slackUserId: String): MutableLiveData<Boolean> {

        val mutableLiveData = MutableLiveData<Boolean>()
        when (val result = remoteData.remindPin(slackUserId)) {
            is ApiResult.Success -> {
                mutableLiveData.postValue(true)
            }
            is ApiResult.Error<*> -> {
                mutableLiveData.postValue(false)
                myErrors.postValue(MyError(ApiError(result.errorResponse.parse(), result.exception)))
            }
        }
        return mutableLiveData
    }
}