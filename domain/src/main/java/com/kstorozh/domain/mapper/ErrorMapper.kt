package com.kstorozh.domain.mapper

import com.kstorozh.dataimpl.DataError
import com.kstorozh.domainapi.model.DomainErrors
import com.kstorozh.domainapi.model.ErrorStatus

class ErrorMapper {
    fun mapToDomainError(error: DataError): DomainErrors {
        return DomainErrors(ErrorStatus.valueOf(error.errorStatus.toString()), error.message, error.throwable)
    }
}