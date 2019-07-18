package com.kstorozh.evozhuk

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kstorozh.domainimpl.GetUsersUseCases
import com.kstorozh.domainimpl.LoginUseCase
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