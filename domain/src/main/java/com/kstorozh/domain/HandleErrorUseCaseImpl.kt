package com.kstorozh.domain

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

    override suspend fun getErrors(): Pair<DomainErrors?, DomainErrors?> {

        val userInput = userRepository.getErrors()
        val userError = if (userInput != null) { mapper.mapToDomainError(userInput) } else null
        val deviceInput = deviceRepository.getErrors()
        val deviceError = if (deviceInput != null) { mapper.mapToDomainError(deviceInput) } else null
        return userError to deviceError
    }
}
