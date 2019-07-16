package com.kstorozh.data.repository

import com.kstorozh.data.models.User
import com.kstorozh.dataimpl.model.into.UserParam
import com.kstorozh.dataimpl.model.out.SlackUser

internal class UserDataMapper {

    fun mapUserParam(userParam: UserParam): User = User(userParam.id,
        userParam.slackId,
        userParam.slackUserName,
        userParam.pin)

    fun mapSlackUserList(users: List<User>): List<SlackUser> {
        val slackUsers = mutableListOf<SlackUser>()
        users.forEach {
            slackUsers.add(SlackUser(it.id, it.slackId, it.slackUserName))
        }
        return slackUsers
    }
}