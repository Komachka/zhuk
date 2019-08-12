package com.kstorozh.dataimpl

data class DataError(
    val errorStatus: ErrorStatus? = null,
    val errorMessage: String? = null,
    val throwable: Throwable

)