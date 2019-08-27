package com.kstorozh.evozhuk.info

import android.os.Build
import androidx.lifecycle.MutableLiveData
import com.kstorozh.domainapi.InfoDeviceUseCases
import com.kstorozh.evozhuk.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.text.DecimalFormat

class InfoViewModel:BaseViewModel(), KoinComponent {

    private val deviceInfo = MutableLiveData<List<Pair<DeviceInfoName, DeviceInfoParam>>>()
    private val applicationScope = CoroutineScope(Dispatchers.Default)
    private val infoUseCase: InfoDeviceUseCases by inject()


    fun getDeviceInfo()
    {
        applicationScope.launch {
            val result = infoUseCase.getDeviceInfo()
            val infoList = listOf(
                INFO_VERSION to result.version,
                INFO_MODEL to result.model,
                INFO_ID to result.id,
                INFO_MEMORY to result.memory,
                INFO_STORAGE to result.storage
            )
            deviceInfo.postValue(infoList)
        }

    }
}