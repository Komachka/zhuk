package com.kstorozh.data.utils

import ACCESS_TOKEN
import INIT_DEVISE_URL
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

internal class AuthInterceptor(private val tokenRepository: TokenRepository) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        if (original.url().encodedPath() != INIT_DEVISE_URL) {
            val currentToken: String? = TokenRepository.token
            currentToken?.let {
                val request = original.newBuilder()
                    .applyAuthorizationHeader(it)
                    .method(original.method(), original.body())
                    .build()
                return chain.proceed(request)
            }
        }
        return chain.proceed(original)
    }

    private fun Request.Builder.applyAuthorizationHeader(currentToken: String) = this.apply {
        header(ACCESS_TOKEN, currentToken)
    }
}
