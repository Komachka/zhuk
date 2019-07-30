package com.kstorozh.domainapi

import com.kstorozh.domainapi.model.User
import com.kstorozh.domainapi.model.UserLoginInput

interface LoginUseCase {

    suspend fun loginUser(user: UserLoginInput): String?
    suspend fun remindPin(user: User): Boolean
}