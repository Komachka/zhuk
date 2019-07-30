package com.kstorozh.domainapi.model

interface GetUsersUseCases {

    suspend fun getUsers(): DomainResult<List<User>>
}