package com.kstorozh.data.models

import TOKEN_TABLE_NAME
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = TOKEN_TABLE_NAME)
internal data class Token(
    @PrimaryKey var id: String,
    val token: String
)