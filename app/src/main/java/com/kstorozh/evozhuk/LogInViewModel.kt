package com.kstorozh.evozhuk

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kstorozh.domain.GetUsersUseCases
import com.kstorozh.domain.LoginUseCase
import com.kstorozh.domainimpl.model.User
import com.kstorozh.domainimpl.model.UserLoginInput

class LogInViewModel(
    val loginUseCase: LoginUseCase,
    val getUserUseCase: GetUsersUseCases
) : ViewModel() {

    fun isLogin(name: String, pass: String) {
        loginUseCase.loginUser(UserLoginInput(name, pass))
    }

    fun getUsers(): LiveData<List<User>> {
        return getUserUseCase.getUsers()
    }
}