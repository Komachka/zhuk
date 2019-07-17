package com.kstorozh.data.repository

import com.kstorozh.data.models.User
import com.kstorozh.data.models.UserLogin
import com.kstorozh.dataimpl.model.UserLoginParam
import com.kstorozh.dataimpl.model.UserParam

internal class UserDataMapper {

    fun mapUserParam(userParam: UserParam): User = User(userParam.id,
        userParam.slackId,
        userParam.slackUserName)

    fun mapUserLoginParam(userLoginParam: UserLoginParam) = UserLogin(userName = userLoginParam.userName, pin = userLoginParam.pin)
}