package com.evo.dataimpl

import com.evo.dataimpl.model.TmpErrorData

data class DataError(
    val errorStatus: ErrorStatus? = null,
    val errorMessage: String? = null,
    val throwable: Throwable,
    val data: TmpErrorData? = null

)