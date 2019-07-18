package com.kstorozh.domainimpl.model

import com.kstorozh.domainimpl.ErrorStatus

class DomainErrors(
    val errorStatus: ErrorStatus? = null,
    val message: String,
    val throwable: Throwable

)