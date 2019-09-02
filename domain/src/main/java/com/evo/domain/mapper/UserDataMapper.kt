package com.evo.domain.mapper

import com.evo.dataimpl.model.UserLoginParam
import com.evo.dataimpl.model.SlackUser
import com.evo.domainapi.model.User
import com.evo.domainapi.model.UserLoginInput

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