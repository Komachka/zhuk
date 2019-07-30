package com.kstorozh.data.database

import TOKEN_TABLE_NAME
import androidx.room.*
import com.kstorozh.data.models.Token

@Dao
internal interface TokenDao {

    @Query("SELECT * FROM $TOKEN_TABLE_NAME LIMIT 1")
    suspend fun getToken(): Token?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToken(token: Token): Long

    @Update
    suspend fun updateToken(token: Token): Int

    @Query("DELETE FROM $TOKEN_TABLE_NAME")
    suspend fun deleteToken()
}