package com.kstorozh.evozhuk


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kstorozh.domainapi.LoginUseCase
import com.kstorozh.domainapi.model.GetUsersUseCases
import com.kstorozh.domainapi.model.User
import com.kstorozh.domainapi.model.UserLoginInput

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