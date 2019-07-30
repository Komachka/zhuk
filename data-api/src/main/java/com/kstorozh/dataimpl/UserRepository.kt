package com.kstorozh.data.repository

import androidx.lifecycle.MutableLiveData
import com.kstorozh.dataimpl.model.UserLoginParam

import com.kstorozh.dataimpl.DataError
import com.kstorozh.dataimpl.model.out.SlackUser

interface UserRepository {
    suspend fun getUsers(): List<SlackUser>
    suspend fun remindPin(slackUserId: String): Boolean
    suspend fun getErrors(): MutableLiveData<DataError>
    suspend fun login(userLoginParam: UserLoginParam): String?
}