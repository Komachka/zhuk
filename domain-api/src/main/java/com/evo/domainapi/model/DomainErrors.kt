package com.evo.domainapi.model

data class DomainErrors(
    val errorStatus: ErrorStatus? = null,
    val throwable: Throwable? = null,
    val message: String? = null,
    val errorData: DomainErrorData? = null
)