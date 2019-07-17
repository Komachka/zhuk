package com.kstorozh.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.kstorozh.data.repository.UserRepository
import com.kstorozh.dataimpl.model.out.SlackUser

class GetUsersUseCases(val repository: UserRepository) {

    fun getUsers() : LiveData<List<SlackUser>>
    {
        return liveData {
            val users = repository.getUsers()
            emitSource(users)
        }
    }
}