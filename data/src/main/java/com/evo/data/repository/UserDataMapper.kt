package com.evo.data.repository

import com.evo.data.models.User
import com.evo.data.models.UserLogin
import com.evo.dataimpl.model.UserLoginParam
import com.evo.dataimpl.model.SlackUser

internal class UserDataMapper {

    fun mapSlackUserList(users: List<User>): List<SlackUser> {
        val slackUsers = mutableListOf<SlackUser>()
        users.forEach {
            slackUsers.add(
                SlackUser(
                    it.id,
                    it.slackUid,
                    it.slackUsername
                )
            )
        }
        return slackUsers
    }

    fun mapUserLoginParam(userLoginParam: UserLoginParam) =
        UserLogin(userName = userLoginParam.userName, pin = userLoginParam.pin)
}