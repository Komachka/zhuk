package com.kstorozh.data.repository

import com.kstorozh.data.database.LocalDataStorage
import com.kstorozh.data.network.RemoteData

class DeviceRepository(
    private val localData: LocalDataStorage,
    private val remoteData: RemoteData
)