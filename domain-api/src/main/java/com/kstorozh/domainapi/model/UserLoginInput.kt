package com.kstorozh.domainapi.model

data class UserLoginInput(
    val userName: String,
    val pin: String
)