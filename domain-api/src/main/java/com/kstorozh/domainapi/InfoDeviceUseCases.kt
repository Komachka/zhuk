package com.kstorozh.domainapi

import com.kstorozh.domainapi.model.DeviceInfo
import com.kstorozh.domainapi.model.DomainResult

interface InfoDeviceUseCases {
    suspend fun getDeviceInfo(): DomainResult<DeviceInfo>
    suspend fun saveNote(note: String): DomainResult<Boolean>
}