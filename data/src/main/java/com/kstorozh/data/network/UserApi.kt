package com.kstorozh.data.network

import CREATE_USER_URL
import GET_USERS_URL
import LOGIN_URL
import REMIND_PIN_URL
import com.kstorozh.data.models.*
import com.kstorozh.data.models.BaseResponse
import com.kstorozh.data.models.LoginUserResponce
import com.kstorozh.data.models.User
import retrofit2.Response
import retrofit2.http.*

internal interface UserApi {
    @GET(GET_USERS_URL)
    suspend fun getUsers(): Response<UsersData>

    @POST(CREATE_USER_URL)
    suspend fun createUser(
        @Body user: User
    ): Response<BaseResponse>

    @GET(REMIND_PIN_URL)
    suspend fun remindPin(
        @Path("id") userId: String
    ): Response<BaseResponse>

    @POST(LOGIN_URL)
    suspend fun login(@Body userLogin: UserLogin): Response<LoginUserResponce>
}