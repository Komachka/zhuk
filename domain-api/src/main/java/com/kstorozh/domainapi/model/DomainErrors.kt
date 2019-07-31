package com.kstorozh.domainapi.model

class DomainErrors(
    val errorStatus: ErrorStatus? = null,
    val throwable: Throwable? = null,
    val message: String? = null

)