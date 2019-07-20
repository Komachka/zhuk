package com.kstorozh.evozhuk.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kstorozh.domainapi.LoginUseCase
import com.kstorozh.domainapi.model.GetUsersUseCases
import com.kstorozh.domainapi.model.User
import com.kstorozh.domainapi.model.UserLoginInput
import org.koin.core.KoinComponent
import org.koin.core.inject

class LogInViewModel : ViewModel(), KoinComponent {

    private val loginUseCase: LoginUseCase by inject()
    private val getUserUseCase: GetUsersUseCases by inject()

    val userIdLiveData:MutableLiveData<String> by lazy { MutableLiveData<String>() }


    private val users: MutableLiveData<List<User>> by lazy {
        MutableLiveData<List<User>>().also {
            loadUsers()
        }
    }

    fun getUsers(): LiveData<List<User>> {
        return users
    }

    private fun loadUsers() {
        liveData<List<User>> {
            getUserUseCase.getUsers()
        }
    }

    fun tryLogin(name: String, pass: String) : LiveData<String> {
        return liveData<String> {
            val userId = loginUseCase.loginUser(UserLoginInput(name, pass))
            userId?.let {
                userIdLiveData.value = userId
                emit(userId)
            }
        }

    }
}