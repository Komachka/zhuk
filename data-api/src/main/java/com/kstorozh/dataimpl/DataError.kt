package com.kstorozh.dataimpl

class DataError(
    val errorStatus: ErrorStatus? = null,
    val errorMessage: String? = null,
    val throwable: Throwable

)