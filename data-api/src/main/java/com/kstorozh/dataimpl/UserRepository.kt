package com.kstorozh.data.repository

import androidx.lifecycle.MutableLiveData
import com.kstorozh.dataimpl.MyErrors
import com.kstorozh.dataimpl.model.into.UserParam
import com.kstorozh.dataimpl.model.out.SlackUser

interface UserRepository {
    suspend fun getUsers(): MutableLiveData<List<SlackUser>>
    suspend fun createUser(userParam: UserParam) //
    suspend fun remindPin(userParam: UserParam) : Boolean
    suspend fun getErrors(): MutableLiveData<MyErrors>
}