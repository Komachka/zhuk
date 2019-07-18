package com.kstorozh.domainimpl

import androidx.lifecycle.LiveData
import com.kstorozh.domainimpl.model.User
import com.kstorozh.domainimpl.model.UserLoginInput

interface LoginUseCase {

    fun loginUser(user: UserLoginInput): LiveData<String>
    fun remindPin(user: User): LiveData<Boolean>
}