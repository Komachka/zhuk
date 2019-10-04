package com.evo.di

import com.evo.data.di.dbModule
import com.evo.data.di.networkModule
import com.evo.data.di.repositoryModule
import com.evo.domain.di.useCaseModules
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