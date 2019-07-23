package com.kstorozh.domain

import androidx.lifecycle.MediatorLiveData
import com.kstorozh.data.repository.UserRepository
import com.kstorozh.dataimpl.DeviseRepository
import com.kstorozh.domain.mapper.ErrorMapper

import com.kstorozh.domainapi.HandleErrorUseCase
import com.kstorozh.domainapi.model.DomainErrors
import org.koin.core.KoinComponent

class HandleErrorUseCaseImpl(
    val userRepository: UserRepository,
    val deviceRepository: DeviseRepository,
    val mapper: ErrorMapper
) : HandleErrorUseCase, KoinComponent {

    private val errors: MediatorLiveData<DomainErrors> = MediatorLiveData()

    override suspend fun getErrors(): MediatorLiveData<DomainErrors> {

        errors.addSource(userRepository.getErrors()) {
            errors.postValue(mapper.mapToDomainError(it))
        }

        errors.addSource(deviceRepository.getErrors()) {
            errors.postValue(mapper.mapToDomainError(it))
        }
        return errors
    }
}
