package com.kstorozh.di

import com.kstorozh.data.di.dbModule
import com.kstorozh.data.di.networkModule
import com.kstorozh.data.di.repositoryModule
import com.kstorozh.domain.di.useCaseModules
import org.koin.core.context.loadKoinModules

object KoinLoader {

    fun loadKoin() =
        loadKoinModules(listOf(
            repositoryModule,
            networkModule,
            dbModule,
            useCaseModules)
        )
}