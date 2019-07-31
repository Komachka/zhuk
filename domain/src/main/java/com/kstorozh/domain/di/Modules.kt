package com.kstorozh.domain.di

import com.kstorozh.data.repository.*

import com.kstorozh.domain.GetUsersUseCasesImpl
import com.kstorozh.domain.LoginUseCaseImpl
import com.kstorozh.domain.ManageDeviceUseCasesImpl
import com.kstorozh.domain.mapper.DeviceInfoMapper
import com.kstorozh.domain.mapper.ErrorMapper
import com.kstorozh.domain.mapper.UserDataMapper
import com.kstorozh.domainapi.LoginUseCase
import com.kstorozh.domainapi.ManageDeviceUseCases
import com.kstorozh.domainapi.model.GetUsersUseCases

import org.koin.dsl.module

// The module is marked as override, which means that its content will override any other definition within the application.

val useCaseModules = module(override = true) {

    factory<GetUsersUseCases> { GetUsersUseCasesImpl(get<UserRepository>(), UserDataMapper(), ErrorMapper()) }
    factory<LoginUseCase> { LoginUseCaseImpl(get(), UserDataMapper(), ErrorMapper()) }
    factory<ManageDeviceUseCases> { ManageDeviceUseCasesImpl(get(), DeviceInfoMapper(), ErrorMapper()) }
}
