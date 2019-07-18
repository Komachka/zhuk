package com.kstorozh.domain

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.liveData
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
    val mediatorLiveData: MediatorLiveData<DomainErrors> = MediatorLiveData()

    override fun getErrors(): MediatorLiveData<DomainErrors> {

        mediatorLiveData.addSource(liveData { emitSource(userRepository.getErrors()) }) {
            mediatorLiveData.postValue(mapper.mapToDomainError(it))
        }
        mediatorLiveData.addSource(liveData { emitSource(deviceRepository.getErrors()) }) {
            mediatorLiveData.postValue(mapper.mapToDomainError(it))
        }
        return mediatorLiveData
    }
}
