package com.kstorozh.data.repository

import com.kstorozh.data.network.RemoteData
import com.kstorozh.dataimpl.model.UserParam

internal class UserRepositoryImpl(
    private val remoteData: RemoteData,
    private val mapper: UserDataMapper
) : UserRepository {
    override suspend fun getUsers() {
        remoteData.getUsers()
    }

    override suspend fun createUser(userParam: UserParam) {

        remoteData.createUser(mapper.mapUserParam(userParam))
    }

    override suspend fun remindPin(userParam: UserParam) {

        remoteData.remindPin(mapper.mapUserParam(userParam))
    }
}