package com.kstorozh.dataimpl.model.out
import com.kstorozh.dataimpl.DataError

class RepoResult<T> (
    val data:T? = null,
    val error: DataError? = null
)