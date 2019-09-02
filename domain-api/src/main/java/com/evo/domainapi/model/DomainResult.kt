package com.evo.domainapi.model

class DomainResult<T>(
    var data: T? = null,
    var domainError: DomainErrors? = null
)