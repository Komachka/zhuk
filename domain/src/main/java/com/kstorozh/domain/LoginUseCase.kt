package com.kstorozh.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.kstorozh.data.repository.UserRepository
import com.kstorozh.dataimpl.model.into.UserLoginInput
import com.kstorozh.dataimpl.model.out.SlackUser

class LoginUseCase(val repository: UserRepository) {

    fun loginUser(user: UserLoginInput): LiveData<Boolean> {
        // TODO handle result of login here
        return liveData {
            emit(false)
        }
    }


    fun remindPin(slackUser:SlackUser) : LiveData<Boolean>
    {
        return liveData {
            repository.remindPin(slackUser.id.toString())
            emit(false)
        }

    }
}