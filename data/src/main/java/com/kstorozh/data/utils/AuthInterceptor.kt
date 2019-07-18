package com.kstorozh.data.utils

import INIT_DEVISE_URL
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor(private val tokenRepository: TokenRepository) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        if (original.url().encodedPath() != INIT_DEVISE_URL) {
            val currentToken = tokenRepository.getToken()
            currentToken?.let {
                val request = original.newBuilder()
                    .addHeaders(currentToken)
                    .method(original.method(), original.body())
                    .build()
                return chain.proceed(request)
            }
        }
        return chain.proceed(original)
    }

    private fun Request.Builder.addHeaders(currentToken: String) = this.apply {
        header("Authorization", currentToken)
    }
}
