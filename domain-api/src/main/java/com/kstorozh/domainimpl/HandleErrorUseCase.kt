package com.kstorozh.domain

import androidx.lifecycle.MediatorLiveData


interface HandleErrorUseCase {
    suspend fun getErrors(): MediatorLiveData<DomainError>
}
