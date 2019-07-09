package com.kstorozh.data.repository

import com.kstorozh.data.User

interface UserRepository {

    suspend fun getUsers()
    suspend fun createUser(user:User)
    suspend fun remindPin(user: User)


}