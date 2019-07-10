package com.kstorozh.data.repository

import com.kstorozh.data.models.User
import com.kstorozh.dataimpl.model.UserParam

internal class UserDataMapper {

    fun mapUserParam(userParam: UserParam): User = User(userParam.id,
        userParam.slackId,
        userParam.slackUserName,
        userParam.pin)
}