package com.evo.domain.di

import com.evo.data.repository.*
import com.evo.domain.*

import com.evo.domain.mapper.CalendarMapper
import com.evo.domain.mapper.DeviceInfoMapper
import com.evo.domain.mapper.ErrorMapper
import com.evo.domain.mapper.UserDataMapper
import com.evo.domainapi.GetBookingUseCase
import com.evo.domainapi.InfoDeviceUseCases
import com.evo.domainapi.LoginUseCase
import com.evo.domainapi.ManageDeviceUseCases
import com.evo.domainapi.model.GetUsersUseCases

import org.koin.dsl.module

// The module is marked as override, which means that its content will override any other definition within the application.

val useCaseModules = module(override = true) {

    single<GetUsersUseCases> { GetUsersUseCasesImpl(get<UserRepository>(), UserDataMapper(), ErrorMapper()) }
    single<LoginUseCase> { LoginUseCaseImpl(get(), UserDataMapper(), ErrorMapper()) }
    single<ManageDeviceUseCases> { ManageDeviceUseCasesImpl(get(), DeviceInfoMapper(), ErrorMapper()) }
    single<GetBookingUseCase> { GetBookingUseCaseImpl(get(), get(), ErrorMapper(), DeviceInfoMapper(), CalendarMapper()) }
    single <InfoDeviceUseCases> { InfoDeviceUseCasesImpl(get(), DeviceInfoMapper(), ErrorMapper()) }
}
