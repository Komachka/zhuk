package com.evo.domainapi

import com.evo.domainapi.model.DeviceInfo
import com.evo.domainapi.model.DomainResult

interface InfoDeviceUseCases {
    suspend fun getDeviceInfo(): DomainResult<DeviceInfo>
    suspend fun saveNote(note: String): DomainResult<Boolean>
}