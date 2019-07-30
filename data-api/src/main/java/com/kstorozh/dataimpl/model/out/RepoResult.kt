package com.kstorozh.dataimpl.model.out
import com.kstorozh.dataimpl.DataError

class RepoResult<T> (
    var data:T? = null,
    var error: DataError? = null
)