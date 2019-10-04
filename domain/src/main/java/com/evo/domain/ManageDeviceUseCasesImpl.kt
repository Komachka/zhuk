package com.evo.domain

import com.evo.dataimpl.DeviseRepository
import com.evo.domain.mapper.DeviceInfoMapper
import com.evo.domain.mapper.ErrorMapper
import com.evo.domainapi.ManageDeviceUseCases
import com.evo.domainapi.model.BookingInputData
import com.evo.domainapi.model.DeviceInputData
import com.evo.domainapi.model.DomainResult
import com.evo.domainapi.model.SessionData
import org.joda.time.format.ISODateTimeFormat
import org.koin.core.KoinComponent

import java.util.*

class ManageDeviceUseCasesImpl(
    private val repository: DeviseRepository,
    val mapper: DeviceInfoMapper,
    val errorMapper: ErrorMapper
) :
    ManageDeviceUseCases, KoinComponent {

    override suspend fun sendReport(state: String, msg: String): DomainResult<Boolean> {
        val repoResult = repository.sendReport(state, msg)
        val domainError = errorMapper.mapToDomainError(repoResult.error)
        return DomainResult(repoResult.data, domainError)
    }

    override suspend fun editCurrentBooking(endDate: Long): DomainResult<Boolean> {
        val foramtter = ISODateTimeFormat.dateTime()
        val end = foramtter.print(endDate)
        val start = foramtter.print(System.currentTimeMillis())

        val repoResult = repository.editCurrentBooking(start, end)
        val domainError = errorMapper.mapToDomainError(repoResult.error)
        return DomainResult(repoResult.data, domainError)
    }

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