package com.evo.domainapi

import com.evo.domainapi.model.DomainResult
import com.evo.domainapi.model.User
import com.evo.domainapi.model.UserLoginInput

interface LoginUseCase {

    suspend fun loginUser(user: UserLoginInput): DomainResult<String>
    suspend fun remindPin(user: User): DomainResult<Boolean>
}