package com.kstorozh.domain.mapper

import android.util.Log
import com.kstorozh.dataimpl.MyError
import com.kstorozh.domainapi.model.DomainErrors
import com.kstorozh.domainapi.model.ErrorStatus
import java.lang.Exception

class ErrorMapper {
    fun mapToDomainError(error: MyError): DomainErrors {
        var status: ErrorStatus? = null
        try {
            status = ErrorStatus.valueOf(error.errorStatus.toString())
        }
        catch (e:Exception)
        {}
            return DomainErrors(status, error.throwable)
    }
}