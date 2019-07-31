package com.kstorozh.data.repository

import com.kstorozh.data.models.ApiResult
import com.kstorozh.data.network.Endpoints
import com.kstorozh.data.network.RemoteData
import com.kstorozh.data.utils.createError
import com.kstorozh.dataimpl.model.UserLoginParam
import com.kstorozh.dataimpl.model.out.RepoResult
import com.kstorozh.dataimpl.model.out.SlackUser
import org.koin.core.KoinComponent

internal class UserRepositoryImpl(
    private val remoteData: RemoteData,
    private val mapper: UserDataMapper

) : UserRepository, KoinComponent {

    override suspend fun login(userLoginParam: UserLoginParam): RepoResult<String> {
        val repoResult: RepoResult<String> = RepoResult()
        return when (val result = remoteData.login(mapper.mapUserLoginParam(userLoginParam))) {
            is ApiResult.Success -> {
                repoResult.data = result.data.data.userId.toString()
                repoResult
            }
            is ApiResult.Error<*> -> {
                repoResult.error = createError(Endpoints.LOGIN, result, this)
                repoResult
            }
        }
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
                repoResult.error = createError(Endpoints.GET_USERS, result, this)
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
                repoResult.data = false
                repoResult.error = createError(Endpoints.REMIND_PIN, result, this)
                repoResult
            }
        }
    }
}