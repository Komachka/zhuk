package com.kstorozh.domainapi

import androidx.lifecycle.LiveData
import com.kstorozh.domainapi.model.User
import com.kstorozh.domainapi.model.UserLoginInput

interface LoginUseCase {

    fun loginUser(user: UserLoginInput): LiveData<String>
    fun remindPin(user: User): LiveData<Boolean>
}