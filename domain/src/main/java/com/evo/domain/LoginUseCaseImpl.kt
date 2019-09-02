package com.evo.domain

import com.evo.data.repository.UserRepository
import com.evo.domain.mapper.ErrorMapper
import com.evo.domain.mapper.UserDataMapper

import com.evo.domainapi.LoginUseCase
import com.evo.domainapi.model.DomainResult
import com.evo.domainapi.model.User
import com.evo.domainapi.model.UserLoginInput
import org.koin.core.KoinComponent

class LoginUseCaseImpl(
    val repository: UserRepository,
    val mapper: UserDataMapper,
    val errorMapper: ErrorMapper
) : LoginUseCase, KoinComponent {

    override suspend fun loginUser(user: UserLoginInput): DomainResult<String> {
        val repoResult = repository.login(mapper.mapLoginInputParams(user))
        val domainError = errorMapper.mapToDomainError(repoResult.error)
        return DomainResult(repoResult.data, domainError)
    }

    override suspend fun remindPin(user: User): DomainResult<Boolean> {
        val res = repository.remindPin(user.id.toString())
        val domainError = errorMapper.mapToDomainError(res.error)
        return DomainResult(res.data, domainError)
    }
}