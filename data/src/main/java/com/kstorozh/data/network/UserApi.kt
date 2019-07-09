package com.kstorozh.data.network

import CREATE_USER_URL
import GET_USERS_URL
import REMIND_PIN_URL
import com.kstorozh.data.User
import retrofit2.http.*

interface UserApi {
    @GET(GET_USERS_URL)
    fun getUsers(@Header("X-ACCESS-TOKEN") token: String = "?"): List<User>

    @POST(CREATE_USER_URL)
    fun createUser(
        @Body user: User,
        @Header("X-ACCESS-TOKEN") token: String = "?"
    )

    @GET(REMIND_PIN_URL)
    fun remindPin(
        @Path("id") userId: String,
        @Header("X-ACCESS-TOKEN") token: String = "?"
    )
}