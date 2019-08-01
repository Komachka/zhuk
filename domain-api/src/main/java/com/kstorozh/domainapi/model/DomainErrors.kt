package com.kstorozh.domainapi.model

data class DomainErrors(
    val errorStatus: ErrorStatus? = null,
    val throwable: Throwable? = null,
    val message: String? = null

)