package com.kstorozh.evozhuk

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kstorozh.domainapi.ManageDeviceUseCases
import com.kstorozh.domainapi.model.DomainErrors
import com.kstorozh.evozhuk.utils.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent

open class BaseViewModel(
    private val reportUseCases: ManageDeviceUseCases,
    private val applicationScope: CoroutineScope
) : ViewModel(), KoinComponent {

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