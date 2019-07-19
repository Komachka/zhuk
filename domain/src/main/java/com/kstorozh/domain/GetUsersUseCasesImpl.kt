package com.kstorozh.domain

import com.kstorozh.data.repository.UserRepository
import com.kstorozh.domain.mapper.UserDataMapper

import com.kstorozh.domainapi.model.GetUsersUseCases
import com.kstorozh.domainapi.model.User
import org.koin.core.KoinComponent

class GetUsersUseCasesImpl(val repository: UserRepository, val mapper: UserDataMapper) :
    GetUsersUseCases, KoinComponent {

    override suspend fun getUsers(): List<User> {

        return mapper.mapListOfSlackUsers(repository.getUsers())
    }
}