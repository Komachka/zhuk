package com.kstorozh.domain.mapper

import com.kstorozh.dataimpl.MyError
import com.kstorozh.domainapi.model.DomainErrors
import com.kstorozh.domainapi.model.ErrorStatus

class ErrorMapper {
    fun mapToDomainError(error: MyError): DomainErrors {
        return DomainErrors(ErrorStatus.valueOf(error.errorStatus.toString()), error.message, error.throwable)
    }
}