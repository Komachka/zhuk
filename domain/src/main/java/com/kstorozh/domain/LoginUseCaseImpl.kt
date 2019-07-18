package com.kstorozh.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
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