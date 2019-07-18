package com.kstorozh.domainimpl

import androidx.lifecycle.MediatorLiveData
import com.kstorozh.domainimpl.model.DomainErrors

interface HandleErrorUseCase {
    fun getErrors(): MediatorLiveData<DomainErrors<*>>
}
