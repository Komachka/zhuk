package com.kstorozh.domain.mapper

import com.kstorozh.dataimpl.MyError
import com.kstorozh.domainimpl.ErrorStatus
import com.kstorozh.domainimpl.model.DomainErrors

class ErrorMapper {
    fun mapToDomainError(error: MyError): DomainErrors {
        return DomainErrors(ErrorStatus.valueOf(error.errorStatus.toString()), error.message, error.throwable)
    }
}