package com.kstorozh.domain

import android.util.Log
import com.kstorozh.data.repository.UserRepository
import com.kstorozh.domain.mapper.UserDataMapper

import com.kstorozh.domainapi.LoginUseCase
import com.kstorozh.domainapi.model.User
import com.kstorozh.domainapi.model.UserLoginInput
import org.koin.core.KoinComponent

class LoginUseCaseImpl(
    val repository: UserRepository,
    val mapper: UserDataMapper
) : LoginUseCase, KoinComponent {

    override suspend fun loginUser(user: UserLoginInput): String? {

            return repository.login(mapper.mapLoginInputParams(user))
    }

    override suspend fun remindPin(user: User): Boolean {
        Log.d("MainActivity", "In use case remind pin user = $user")
        val res = repository.remindPin(user.id.toString())
        Log.d("MainActivity", "Result remind pin $res")
        return res
    }
}