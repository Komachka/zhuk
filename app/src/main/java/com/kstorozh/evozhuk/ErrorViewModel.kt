package com.kstorozh.evozhuk

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kstorozh.domainapi.HandleErrorUseCase
import com.kstorozh.domainapi.model.DomainErrors
import org.koin.core.KoinComponent
import org.koin.core.inject

class ErrorViewModel : ViewModel(), KoinComponent {

    private val errorHandler: HandleErrorUseCase by inject()

    fun getErrors(): LiveData<DomainErrors> {
        return liveData {
            val errros = errorHandler.getErrors()
            emitSource(errros)
        }
    }
}