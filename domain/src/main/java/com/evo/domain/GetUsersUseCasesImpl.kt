package com.evo.domain

import com.evo.data.repository.UserRepository
import com.evo.domain.mapper.ErrorMapper
import com.evo.domain.mapper.UserDataMapper
import com.evo.domainapi.model.DomainResult

import com.evo.domainapi.model.GetUsersUseCases
import com.evo.domainapi.model.User
import org.koin.core.KoinComponent

class GetUsersUseCasesImpl(
    val repository: UserRepository,
    val mapper: UserDataMapper,
    val errorMapper: ErrorMapper
) :
    GetUsersUseCases, KoinComponent {

    override suspend fun getUsers(): DomainResult<List<User>> {
        val repoResult = repository.getUsers()
        val domainError = errorMapper.mapToDomainError(repoResult.error)
        val data = repoResult.data?.let { mapper.mapListOfSlackUsers(it) }
        return DomainResult(data, domainError)
    }
}