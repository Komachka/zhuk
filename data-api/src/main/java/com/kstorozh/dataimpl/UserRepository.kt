package com.kstorozh.data.repository

import androidx.lifecycle.MutableLiveData
import com.kstorozh.dataimpl.model.UserLoginParam

import com.kstorozh.dataimpl.DataError
import com.kstorozh.dataimpl.model.out.RepoResult
import com.kstorozh.dataimpl.model.out.SlackUser

interface UserRepository {
    suspend fun getUsers(): RepoResult<List<SlackUser>>
    suspend fun remindPin(slackUserId: String): RepoResult<Boolean>
    suspend fun login(userLoginParam: UserLoginParam): RepoResult<String>
}