package com.kstorozh.evozhuk.login

import androidx.arch.core.util.Function
import androidx.lifecycle.*
import com.kstorozh.domainapi.LoginUseCase
import com.kstorozh.domainapi.model.GetUsersUseCases
import com.kstorozh.domainapi.model.User
import com.kstorozh.domainapi.model.UserLoginInput
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class LogInViewModel : ViewModel(), KoinComponent {

    private val loginUseCase: LoginUseCase by inject()
    private val getUserUseCase: GetUsersUseCases by inject()

    val userIdLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    val users: MutableLiveData<List<User>> by lazy { MutableLiveData<List<User>>().also {
        loadUsers()
    } }

    fun getUserNames(): LiveData<ArrayList<String>> {
        return Transformations.map(users, Function<List<User>, ArrayList<String>> {
            val names: ArrayList<String> = ArrayList()
            it.forEach {
                names.add(it.slackUserName)
            }
            return@Function names
        })
    }

    fun getUserByName(login: String): LiveData<User?> {

        return Transformations.map(users, Function<List<User>, User?> {
            var user: User? = null
            it.forEach {
                if (it.slackUserName == login) user = it
            }
            return@Function user
        })
    }

    private fun loadUsers() {

        GlobalScope.launch {
            val data = getUserUseCase.getUsers()
            users.postValue(data)
        }
    }

    fun tryLogin(name: String, pass: String): LiveData<String> {
        return liveData<String> {
            val userId = loginUseCase.loginUser(UserLoginInput(name, pass))
            userId?.let {
                userIdLiveData.value = userId
                emit(userId)
            }
        }
    }

    fun remindPin(user: User): LiveData<Boolean> {

        return liveData {
            emit(loginUseCase.remindPin(user))
        }
    }
}