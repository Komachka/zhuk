package com.kstorozh.evozhuk

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kstorozh.domainapi.ManageDeviceUseCases
import com.kstorozh.domainapi.model.DomainErrors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

open class BaseViewModel : ViewModel(), KoinComponent {

    private val reportUseCases: ManageDeviceUseCases by inject()
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    fun sendReport(state: String, msg: String): LiveData<Event<Boolean>> {
        val tryLoginLiveData: MutableLiveData<Event<Boolean>> = MutableLiveData<Event<Boolean>>()
        applicationScope.launch {
            if (msg.isNotEmpty()) {
                val domainRes = reportUseCases.sendReport(state, msg)
                domainRes.data?.let {
                    tryLoginLiveData.postValue(Event(it))
                }
                domainRes.domainError?.let {
                    errors.postValue(Event(it))
                }
            }
        }
        return tryLoginLiveData
    }

    val errors = MutableLiveData<Event<DomainErrors>>()
}