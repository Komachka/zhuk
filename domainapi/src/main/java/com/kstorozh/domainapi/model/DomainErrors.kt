package com.kstorozh.domainapi.model

class DomainErrors(
    val errorStatus: ErrorStatus? = null,
    val message: String,
    val throwable: Throwable

)