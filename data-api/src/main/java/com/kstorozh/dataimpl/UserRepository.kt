package com.kstorozh.data.repository

import com.kstorozh.dataimpl.model.UserParam

interface UserRepository {

    suspend fun getUsers()
    suspend fun createUser(userParam: UserParam)
    suspend fun remindPin(userParam: UserParam)
}