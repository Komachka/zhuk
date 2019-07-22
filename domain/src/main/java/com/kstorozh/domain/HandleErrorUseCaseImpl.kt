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
        val userError = if (userInput.isNotEmpty()) { mapper.mapToDomainError(userInput[0]) } else null
        val deviceInput = deviceRepository.getErrors()
        val deviceError = if (userInput.isNotEmpty()) { mapper.mapToDomainError(deviceInput[0]) } else null
        return userError to deviceError
    }
}
