package com.kstorozh.data.utils

internal class TokenRepository {

        companion object {
                private var token: String? = null
        }

        fun setToken(token: String) {
                TokenRepository.token = token
            print(token) // TODO set token to sharedPref? or BD
        }

        fun getToken(): String? = token // TOD get token from shared pref or bd
}