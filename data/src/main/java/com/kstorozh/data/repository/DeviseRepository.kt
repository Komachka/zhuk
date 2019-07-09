package com.kstorozh.domain

interface DeviseRepository {
    suspend fun initDevice()
    suspend fun updateDevice()


}