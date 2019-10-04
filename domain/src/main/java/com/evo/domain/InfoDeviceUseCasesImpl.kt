package com.evo.domain

import com.evo.dataimpl.DeviseRepository
import com.evo.domain.mapper.DeviceInfoMapper
import com.evo.domain.mapper.ErrorMapper
import com.evo.domainapi.InfoDeviceUseCases
import com.evo.domainapi.model.DeviceInfo
import com.evo.domainapi.model.DomainResult

class InfoDeviceUseCasesImpl(
    val repository: DeviseRepository,
    val mapper: DeviceInfoMapper,
    val errorMapper: ErrorMapper
) : InfoDeviceUseCases {
    override suspend fun saveNote(note: String): DomainResult<Boolean> {
        val repoResult = repository.saveNote(note)
        val domainError = errorMapper.mapToDomainError(repoResult.error)
        return DomainResult(repoResult.data, domainError)
    }

    override suspend fun getDeviceInfo(): DomainResult<DeviceInfo> {
        val repoResult = repository.getDeviceInfo()
        val domainError = errorMapper.mapToDomainError(repoResult.error)
        val data = repoResult.data?.let { mapper.mapToDeviceInfo(it) }
        return DomainResult(data, domainError)
    }
}