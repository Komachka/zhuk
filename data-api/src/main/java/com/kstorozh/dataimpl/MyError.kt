package com.kstorozh.dataimpl

class MyError(
    val errorStatus: ErrorStatus? = null,
    val message: String,
    val throwable: Throwable

)