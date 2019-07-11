package com.kstorozh.data.utils

import BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal class MyRetrofit {
    companion object {
        fun create(): Retrofit {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(
                    GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit
        }
    }
}