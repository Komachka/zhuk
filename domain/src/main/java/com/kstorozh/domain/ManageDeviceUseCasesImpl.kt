package com.kstorozh.domain

import com.kstorozh.dataimpl.DeviseRepository
import com.kstorozh.domain.mapper.DeviceInfoMapper
import com.kstorozh.domain.mapper.ErrorMapper
import com.kstorozh.domainapi.ManageDeviceUseCases
import com.kstorozh.domainapi.model.BookingInputData
import com.kstorozh.domainapi.model.DeviceInputData
import com.kstorozh.domainapi.model.DomainResult
import com.kstorozh.domainapi.model.SessionData
import org.koin.core.KoinComponent

import java.util.*

class ManageDeviceUseCasesImpl(
    private val repository: DeviseRepository,
    val mapper: DeviceInfoMapper,
    val errorMapper: ErrorMapper
) :
    ManageDeviceUseCases, KoinComponent {

    override suspend fun isDeviceInited(deviceInputData: DeviceInputData): DomainResult<Boolean> {
        val deviceParam = mapper.mapDeviceInfoToDeviceParam(deviceInputData)
        val repoResult = repository.deviceAlreadyInited(deviceParam)
        val domainError = errorMapper.mapToDomainError(repoResult.error)
        return DomainResult(repoResult.data, domainError)
    }

    override suspend fun getSession(): DomainResult<SessionData> {
        val repoResult = repository.getBookingSession()
        val domainResult: DomainResult<SessionData> = DomainResult()

        repoResult.data?.let {
            domainResult.data = mapper.mapBookingSession(it)
        }
        repoResult.error?.let {
            domainResult.domainError = errorMapper.mapToDomainError(repoResult.error)
        }
        return domainResult
    }

    override suspend fun initDevice(deviceInputData: DeviceInputData): DomainResult<Boolean> {

        val deviceParam = mapper.mapDeviceInfoToDeviceParam(deviceInputData)
        val repoResult = repository.initDevice(deviceParam)
        val domainError = errorMapper.mapToDomainError(repoResult.error)
        return DomainResult(repoResult.data, domainError)
    }

    override suspend fun takeDevice(bookingParam: BookingInputData): DomainResult<Boolean> {

        val startDate = Calendar.getInstance()
        val repoResult = repository.takeDevice(mapper.mapBookingParam(bookingParam, startDate))
        val domainError = errorMapper.mapToDomainError(repoResult.error)
        return DomainResult(repoResult.data, domainError)
    }

    override suspend fun returnDevice(bookingParam: BookingInputData): DomainResult<Boolean> {
        val booking = mapper.mapBookingParam(bookingParam)
        val repoResult = repository.returnDevice(booking)
        val domainError = errorMapper.mapToDomainError(repoResult.error)
        return DomainResult(repoResult.data, domainError)
    }
}