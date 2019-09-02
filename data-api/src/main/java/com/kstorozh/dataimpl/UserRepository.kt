package com.kstorozh.data.repository

import com.kstorozh.dataimpl.model.UserLoginParam

import com.kstorozh.dataimpl.model.RepoResult
import com.kstorozh.dataimpl.model.SlackUser

interface UserRepository {
    suspend fun getUsers(): RepoResult<List<SlackUser>>
    suspend fun remindPin(slackUserId: String): RepoResult<Boolean>
    suspend fun login(userLoginParam: UserLoginParam): RepoResult<String>
}