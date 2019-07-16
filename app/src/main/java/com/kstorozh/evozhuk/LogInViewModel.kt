package com.kstorozh.evozhuk

import androidx.lifecycle.ViewModel
import com.kstorozh.domain.LoginUseCase

class LogInViewModel(val loginUseCase: LoginUseCase) : ViewModel() {

    fun isLogin(name: String, pass: String) {
        //loginUseCase.loginUser()
    }
}