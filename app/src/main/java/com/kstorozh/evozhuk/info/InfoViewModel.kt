package com.kstorozh.evozhuk.info

import androidx.lifecycle.MutableLiveData
import com.kstorozh.domainapi.InfoDeviceUseCases
import com.kstorozh.evozhuk.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class InfoViewModel : BaseViewModel(), KoinComponent {

    val deviceInfo = MutableLiveData<List<Pair<DeviceInfoName, DeviceInfoParam>>>()
    private val applicationScope = CoroutineScope(Dispatchers.Default)
    private val infoUseCase: InfoDeviceUseCases by inject()

    fun getDeviceInfo() {
        applicationScope.launch {
            val result = infoUseCase.getDeviceInfo()
            result.data?.let {
                val infoList = listOf(
                    INFO_MODEL to it.model,
                    INFO_VERSION to it.version,
                    INFO_MEMORY to it.memory,
                    INFO_STORAGE to it.storage,
                    INFO_STORAGE_EMPTY to it.storageEmpty,
                    INFO_MAC to it.id,
                    INFO_NOTE to it.note
                )
                deviceInfo.postValue(infoList)
            }
            result.domainError?.let {
                errors.postValue(Event(it))
            }
        }
    }

    fun saveNote(note: String) {
        applicationScope.launch {
            val result = infoUseCase.saveNote(note)
            result.domainError?.let {
                errors.postValue(Event(it))
            }
        }
    }
}