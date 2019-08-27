package com.kstorozh.domain

import com.kstorozh.dataimpl.DeviseRepository
import com.kstorozh.domain.mapper.DeviceInfoMapper
import com.kstorozh.domain.mapper.ErrorMapper
import com.kstorozh.domainapi.InfoDeviceUseCases
import com.kstorozh.domainapi.model.DeviceInfo
import com.kstorozh.domainapi.model.DomainResult

class InfoDeviceUseCasesImpl(val repository: DeviseRepository,
                             val mapper: DeviceInfoMapper,
                             val errorMapper: ErrorMapper
):InfoDeviceUseCases {



    override suspend fun getDeviceInfo(): DomainResult<DeviceInfo> {
        val result = repository.getDeviceInfo()
    }
}