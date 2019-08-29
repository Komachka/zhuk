package com.kstorozh.dataimpl

import com.kstorozh.dataimpl.model.TmpErrorData

data class DataError(
    val errorStatus: ErrorStatus? = null,
    val errorMessage: String? = null,
    val throwable: Throwable,
    val data: TmpErrorData? = null

)