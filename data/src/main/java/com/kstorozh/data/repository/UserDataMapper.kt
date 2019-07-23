package com.kstorozh.data.repository

import com.kstorozh.data.models.User
import com.kstorozh.data.models.UserLogin
import com.kstorozh.dataimpl.model.UserLoginParam
import com.kstorozh.dataimpl.model.into.UserParam
import com.kstorozh.dataimpl.model.out.SlackUser

internal class UserDataMapper {

    fun mapUserParam(userParam: UserParam): User = User(
        userParam.id,
        userParam.slackId,
        userParam.slackUserName
    )

    fun mapSlackUserList(users: List<User>): List<SlackUser> {
        val slackUsers = mutableListOf<SlackUser>()
        users.forEach {
            slackUsers.add(SlackUser(it.id, it.slackUid, it.slackUsername))
        }
        return slackUsers
    }

    fun mapUserLoginParam(userLoginParam: UserLoginParam) =
        UserLogin(userName = userLoginParam.userName, pin = userLoginParam.pin)
}