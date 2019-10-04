package com.evo.data.network

import ACCESS_TOKEN
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

internal class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val currentToken: String? = TokenRepository.token
            currentToken?.let {
                val request = original.newBuilder()
                    .applyAuthorizationHeader(it)
                    .method(original.method(), original.body())
                    .build()
                return chain.proceed(request)
            }
        return chain.proceed(original)
    }

    private fun Request.Builder.applyAuthorizationHeader(currentToken: String) = this.apply {
        header(ACCESS_TOKEN, currentToken)
    }
}
