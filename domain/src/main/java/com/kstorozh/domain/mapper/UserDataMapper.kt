package com.kstorozh.domain.mapper

import com.kstorozh.dataimpl.model.UserLoginParam
import com.kstorozh.dataimpl.model.SlackUser
import com.kstorozh.domainapi.model.User
import com.kstorozh.domainapi.model.UserLoginInput

class UserDataMapper {
    fun mapLoginInputParams(userLoginInput: UserLoginInput) =
        UserLoginParam(userLoginInput.userName, userLoginInput.pin)

    fun mapListOfSlackUsers(slackUsers: List<SlackUser>): List<User> {
        val users = mutableListOf<User>()
        slackUsers.forEach {
            users.add(User(it.id, it.slackId, it.slackUserName))
        }
        return users
    }
}