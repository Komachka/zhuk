package com.kstorozh.data.network

import GET_USERS_URL
import LOGIN_URL
import REMIND_PIN_URL
import com.kstorozh.data.models.*
import com.kstorozh.data.models.BaseResponse
import com.kstorozh.data.models.LoginUserResponse
import retrofit2.Response
import retrofit2.http.*

internal interface UserApi {
    @GET(GET_USERS_URL)
    suspend fun getUsers(): Response<UsersData>

    @GET(REMIND_PIN_URL)
    suspend fun remindPin(
        @Path("id") userId: String
    ): Response<BaseResponse>

    @POST(LOGIN_URL)
    suspend fun login(@Body userLogin: UserLogin): Response<LoginUserResponse>
}