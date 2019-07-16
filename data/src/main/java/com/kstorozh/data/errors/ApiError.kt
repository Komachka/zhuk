package com.kstorozh.data.errors

import com.kstorozh.data.models.ApiErrorBody
import com.kstorozh.dataimpl.MyErrors

internal data class ApiError(
    val body: ApiErrorBody,
    val exception: Exception
) : MyErrors {
    override fun showError() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}
