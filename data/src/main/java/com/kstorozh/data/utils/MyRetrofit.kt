package com.kstorozh.data.utils

import BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient

internal class MyRetrofit {
    companion object {
        private var retrofit: Retrofit? = null
        private var okHttpClient: OkHttpClient? = null

        fun create(interceptor: AuthInterceptor): Retrofit? {
            if (okHttpClient == null) {
                okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
            }

            if (retrofit == null) {
                okHttpClient?.let {
                    retrofit = Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(BASE_URL)
                        .client(okHttpClient)
                        .build()
                }
            }
        return retrofit
        }
    }
}
