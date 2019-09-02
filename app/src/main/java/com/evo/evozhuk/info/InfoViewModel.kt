package com.evo.evozhuk.info

import androidx.lifecycle.MutableLiveData
import com.evo.domainapi.InfoDeviceUseCases
import com.evo.domainapi.ManageDeviceUseCases
import com.evo.evozhuk.*
import com.evo.evozhuk.utils.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent

class InfoViewModel(
    private val applicationScope: CoroutineScope,
    private val infoUseCase: InfoDeviceUseCases,
    reportUseCase: ManageDeviceUseCases
) : BaseViewModel(reportUseCase, applicationScope), KoinComponent {

    val deviceInfo = MutableLiveData<List<Pair<DeviceInfoName, DeviceInfoParam>>>()

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