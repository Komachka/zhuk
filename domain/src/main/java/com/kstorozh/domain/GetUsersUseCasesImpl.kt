package com.kstorozh.domain

import com.kstorozh.data.repository.UserRepository
import com.kstorozh.domain.mapper.ErrorMapper
import com.kstorozh.domain.mapper.UserDataMapper
import com.kstorozh.domainapi.model.DomainResult

import com.kstorozh.domainapi.model.GetUsersUseCases
import com.kstorozh.domainapi.model.User
import org.koin.core.KoinComponent

class GetUsersUseCasesImpl(val repository: UserRepository,
                           val mapper: UserDataMapper,
                           val errorMapper: ErrorMapper) :
    GetUsersUseCases, KoinComponent {

    override suspend fun getUsers(): DomainResult<List<User>> {
        val repoResult = repository.getUsers()
        val domainError = errorMapper.mapToDomainError(repoResult.error)
        val data = repoResult.data?.let { mapper.mapListOfSlackUsers(it) }
        return DomainResult(data, domainError)
    }
}