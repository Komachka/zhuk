package com.kstorozh.data.errors

import com.kstorozh.dataimpl.MyErrors

class DbError(val exception: Exception) : MyErrors {
    override fun showError() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}