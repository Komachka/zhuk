package com.kstorozh.domain

import android.util.Log
import com.kstorozh.data.repository.UserRepository
import com.kstorozh.domain.mapper.ErrorMapper
import com.kstorozh.domain.mapper.UserDataMapper

import com.kstorozh.domainapi.LoginUseCase
import com.kstorozh.domainapi.model.DomainResult
import com.kstorozh.domainapi.model.User
import com.kstorozh.domainapi.model.UserLoginInput
import org.koin.core.KoinComponent

class LoginUseCaseImpl(
    val repository: UserRepository,
    val mapper: UserDataMapper,
    val errorMapper:ErrorMapper
) : LoginUseCase, KoinComponent {

    override suspend fun loginUser(user: UserLoginInput): DomainResult<String> {
        val repoResult = repository.login(mapper.mapLoginInputParams(user))
        val domainError = errorMapper.mapToDomainError(repoResult.error)
        return DomainResult(repoResult.data, domainError)
    }

    override suspend fun remindPin(user: User): DomainResult<Boolean> {
        Log.d("MainActivity", "In use case remind pin user = $user")
        val res = repository.remindPin(user.id.toString())
        val domainError = errorMapper.mapToDomainError(res.error)
        return DomainResult(res.data, domainError)
    }
}