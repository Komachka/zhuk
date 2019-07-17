package com.kstorozh.data.repository

import androidx.lifecycle.LiveData
import com.kstorozh.dataimpl.model.UserLoginParam

import androidx.lifecycle.MutableLiveData
import com.kstorozh.dataimpl.MyError
import com.kstorozh.dataimpl.model.into.UserParam
import com.kstorozh.dataimpl.model.out.SlackUser

interface UserRepository {
    suspend fun getUsers(): MutableLiveData<List<SlackUser>>
    suspend fun createUser(userParam: UserParam) // TODO delete later
    suspend fun remindPin(slackUserId: String): LiveData<Boolean>
    suspend fun getErrors(): MutableLiveData<MyError<*>>
    suspend fun login(userLoginParam: UserLoginParam): LiveData<String?>
}