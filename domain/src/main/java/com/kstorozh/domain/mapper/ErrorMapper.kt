package com.kstorozh.domain.mapper

import com.kstorozh.dataimpl.MyErrors
import com.kstorozh.domainimpl.model.DomainErrors

class ErrorMapper {
    fun <T> mapToDomainError(error: MyErrors<T>): DomainErrors<T> {
        return DomainErrors(error.data, "domain layer error handling")
    }
}