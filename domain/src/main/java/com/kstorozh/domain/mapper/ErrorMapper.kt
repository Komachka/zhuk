package com.kstorozh.domain.mapper

import com.kstorozh.dataimpl.MyError
import com.kstorozh.domainimpl.model.DomainErrors

class ErrorMapper {
    fun <T> mapToDomainError(error: MyError<T>): DomainErrors<T> {
        return DomainErrors(error.data, "domain layer error handling")
    }
}