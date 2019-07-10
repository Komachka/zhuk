package com.kstorozh.data.network

import CREATE_USER_URL
import GET_USERS_URL
import REMIND_PIN_URL
import com.kstorozh.data.models.User
import com.kstorozh.data.models.UsersDataResponse
import retrofit2.Response
import retrofit2.http.*

internal interface UserApi {
    @GET(GET_USERS_URL)
    fun getUsers(): Response<UsersDataResponse>

    @POST(CREATE_USER_URL)
    fun createUser(
        @Body user: User
    )

    @GET(REMIND_PIN_URL)
    fun remindPin(
        @Path("id") userId: String
    )
}