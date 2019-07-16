package com.kstorozh.domain

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.liveData
import com.kstorozh.data.repository.UserRepository
import com.kstorozh.dataimpl.DeviseRepository
import com.kstorozh.dataimpl.MyErrors

class HandleErrorUseCase(
    val userRepository: UserRepository,
    val deviceRepository: DeviseRepository
) {
    val mediatorLiveData: MediatorLiveData<MyErrors> = MediatorLiveData()

    init {
        mediatorLiveData.addSource(
            liveData { emitSource(userRepository.getErrors()) }) {
            mediatorLiveData.value = it
        }
        mediatorLiveData.addSource(
            liveData { emitSource(deviceRepository.getErrors()) }) {
            mediatorLiveData.value = it
        }
    }
}