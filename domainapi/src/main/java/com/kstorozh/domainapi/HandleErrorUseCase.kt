package com.kstorozh.domainapi

import androidx.lifecycle.MediatorLiveData
import com.kstorozh.domainapi.model.DomainErrors

interface HandleErrorUseCase {
    suspend fun getErrors(): MediatorLiveData<DomainErrors>
}
