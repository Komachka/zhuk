package com.evo.dataimpl.model
import com.evo.dataimpl.DataError

class RepoResult<T> (
    var data: T? = null,
    var error: DataError? = null
)