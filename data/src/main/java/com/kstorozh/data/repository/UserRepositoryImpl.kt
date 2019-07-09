package com.kstorozh.data.repository

import com.kstorozh.data.User
import com.kstorozh.data.network.RemoteData

class UserRepositoryImpl(val remoteData: RemoteData) : UserRepository {
    override suspend fun getUsers() {
        remoteData.getUsers();
    }

    override suspend fun createUser(user: User) {
        remoteData.createUser(user)
    }

    override suspend fun remindPin(user: User) {
        remoteData.remindPin(user)
    }
}