package com.kstorozh.data.repository

import com.kstorozh.dataimpl.model.UserLoginParam

import com.kstorozh.dataimpl.MyError
import com.kstorozh.dataimpl.model.out.SlackUser

interface UserRepository {
    suspend fun getUsers(): List<SlackUser>
    suspend fun remindPin(slackUserId: String): Boolean
    suspend fun getErrors(): MyError?
    suspend fun login(userLoginParam: UserLoginParam): String?
}