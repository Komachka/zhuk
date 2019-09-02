package com.evo.domainapi.model

interface GetUsersUseCases {

    suspend fun getUsers(): DomainResult<List<User>>
}