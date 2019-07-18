package com.kstorozh.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.kstorozh.data.repository.UserRepository
import com.kstorozh.domain.mapper.UserDataMapper
import com.kstorozh.domainimpl.LoginUseCase
import com.kstorozh.domainimpl.model.User
import com.kstorozh.domainimpl.model.UserLoginInput

class LoginUseCaseImpl(
    val repository: UserRepository,
    val mapper: UserDataMapper
) : LoginUseCase {

    override fun loginUser(user: UserLoginInput): LiveData<String> {
        return liveData {
            val userId = repository.login(mapper.mapLoginInputParams(user))
            userId.let {
                emit(it.value!!)
            }
        }
    }

    override fun remindPin(user: User): LiveData<Boolean> {
        return liveData {
            val isPinReminded = repository.remindPin(user.id.toString())
            emitSource(isPinReminded)
        }
    }
}