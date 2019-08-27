package com.kstorozh.domain.di

import com.kstorozh.data.repository.*
import com.kstorozh.domain.*

import com.kstorozh.domain.mapper.CalendarMapper
import com.kstorozh.domain.mapper.DeviceInfoMapper
import com.kstorozh.domain.mapper.ErrorMapper
import com.kstorozh.domain.mapper.UserDataMapper
import com.kstorozh.domainapi.GetBookingUseCase
import com.kstorozh.domainapi.InfoDeviceUseCases
import com.kstorozh.domainapi.LoginUseCase
import com.kstorozh.domainapi.ManageDeviceUseCases
import com.kstorozh.domainapi.model.GetUsersUseCases

import org.koin.dsl.module

// The module is marked as override, which means that its content will override any other definition within the application.

val useCaseModules = module(override = true) {

    factory<GetUsersUseCases> { GetUsersUseCasesImpl(get<UserRepository>(), UserDataMapper(), ErrorMapper()) }
    factory<LoginUseCase> { LoginUseCaseImpl(get(), UserDataMapper(), ErrorMapper()) }
    factory<ManageDeviceUseCases> { ManageDeviceUseCasesImpl(get(), DeviceInfoMapper(), ErrorMapper()) }
    factory<GetBookingUseCase> { GetBookingUseCaseImpl(get(), get(), ErrorMapper(), DeviceInfoMapper(), CalendarMapper()) }
    factory <InfoDeviceUseCases>{InfoDeviceUseCasesImpl(get(), DeviceInfoMapper(), ErrorMapper() ) }
}
