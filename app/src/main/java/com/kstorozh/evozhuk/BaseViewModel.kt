package com.kstorozh.evozhuk

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kstorozh.domainapi.model.DomainErrors

open class BaseViewModel : ViewModel() {
    val errors: MutableLiveData<DomainErrors> = MutableLiveData<DomainErrors>()
}