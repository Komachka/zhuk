package com.kstorozh.domainimpl

import androidx.lifecycle.LiveData
import com.kstorozh.domainimpl.model.User

interface GetUsersUseCases {

    fun getUsers(): LiveData<List<User>>
}