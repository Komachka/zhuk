package com.kstorozh.domainapi.model

interface GetUsersUseCases {

    suspend fun getUsers(): List<User>
}