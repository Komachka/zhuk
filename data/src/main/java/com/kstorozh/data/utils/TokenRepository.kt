package com.kstorozh.data.utils

import LOG_TAG
import android.util.Log
import com.kstorozh.data.database.TokenDao
import com.kstorozh.data.models.Token

internal class TokenRepository(val tokenDao: TokenDao) {
    companion object {
        var token: String = "token"
    }

    suspend fun setToken(tokenFromDb: String) {
            tokenDao.insertToken(token = Token("1", tokenFromDb))
            token = tokenFromDb
            Log.d(LOG_TAG, "token in set token $token")
        }

    suspend fun getToken(): String? {
                val tokenDb = tokenDao.getToken()
                token = tokenDb?.token ?: "no token"
                return tokenDb?.token ?: null
        }
}