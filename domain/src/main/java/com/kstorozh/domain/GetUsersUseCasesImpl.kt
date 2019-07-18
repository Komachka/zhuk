package com.kstorozh.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.kstorozh.data.repository.UserRepository
import com.kstorozh.domain.mapper.UserDataMapper

import com.kstorozh.domainapi.model.GetUsersUseCases
import com.kstorozh.domainapi.model.User
import org.koin.core.KoinComponent


class GetUsersUseCasesImpl(val repository: UserRepository, val mapper: UserDataMapper) :
    GetUsersUseCases, KoinComponent {

    override fun getUsers(): LiveData<List<User>> {
        return liveData {
            val users = repository.getUsers()
            emit(mapper.mapListOfSlackUsers(users.value!!))
        }
    }
}