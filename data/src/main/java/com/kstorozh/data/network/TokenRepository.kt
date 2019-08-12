package com.kstorozh.data.network

import com.kstorozh.data.database.TokenDao
import com.kstorozh.data.models.Token

internal class TokenRepository(private val tokenDao: TokenDao) {
    companion object {
        var token: String? = null
    }

    suspend fun setToken(tokenFromDb: String) {
            tokenDao.insertToken(token = Token("1", tokenFromDb))
            token = tokenFromDb
        }

    suspend fun getToken(): String? {
                val tokenDb = tokenDao.getToken()
                tokenDb?.let {
                    token = it.token
                }
                return tokenDb?.token
    }
}