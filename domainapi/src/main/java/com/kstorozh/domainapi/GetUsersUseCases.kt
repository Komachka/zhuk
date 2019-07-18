package com.kstorozh.domainapi.model

import androidx.lifecycle.LiveData

interface GetUsersUseCases {

    fun getUsers(): LiveData<List<User>>
}