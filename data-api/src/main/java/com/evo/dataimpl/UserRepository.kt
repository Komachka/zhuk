package com.evo.data.repository

import com.evo.dataimpl.model.UserLoginParam

import com.evo.dataimpl.model.RepoResult
import com.evo.dataimpl.model.SlackUser

interface UserRepository {
    suspend fun getUsers(): RepoResult<List<SlackUser>>
    suspend fun remindPin(slackUserId: String): RepoResult<Boolean>
    suspend fun login(userLoginParam: UserLoginParam): RepoResult<String>
}