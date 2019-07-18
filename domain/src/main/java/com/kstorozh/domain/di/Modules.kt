package com.kstorozh.domain.di


import androidx.lifecycle.MutableLiveData

import com.kstorozh.data.repository.*


import com.kstorozh.dataimpl.DeviseRepository
import com.kstorozh.dataimpl.MyError
import com.kstorozh.dataimpl.model.out.SlackUser
import com.kstorozh.domain.GetUsersUseCasesImpl
import com.kstorozh.domain.HandleErrorUseCaseImpl
import com.kstorozh.domain.LoginUseCaseImpl
import com.kstorozh.domain.ManageDeviceUseCasesImpl
import com.kstorozh.domain.mapper.DeviceInfoMapper
import com.kstorozh.domain.mapper.ErrorMapper
import com.kstorozh.domain.mapper.UserDataMapper
import com.kstorozh.domainimpl.GetUsersUseCases
import com.kstorozh.domainimpl.HandleErrorUseCase
import com.kstorozh.domainimpl.LoginUseCase
import com.kstorozh.domainimpl.ManageDeviceUseCases

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


// The module is marked as override, which means that its content will override any other definition within the application.

val useCaseModules = module(override = true) {

    factory<GetUsersUseCases> { GetUsersUseCasesImpl(get<UserRepository>(), UserDataMapper() ) }
    factory<HandleErrorUseCase> {HandleErrorUseCaseImpl(get(), get(), ErrorMapper())  }
    factory<LoginUseCase> { LoginUseCaseImpl(get(),  UserDataMapper()) }
    factory<ManageDeviceUseCases> { ManageDeviceUseCasesImpl(get(),  DeviceInfoMapper())  }
}
