package com.kstorozh.evozhuk.login

import androidx.arch.core.util.Function
import androidx.lifecycle.*
import com.kstorozh.domainapi.LoginUseCase
import com.kstorozh.domainapi.ManageDeviceUseCases
import com.kstorozh.domainapi.model.*
import com.kstorozh.evozhuk.BaseViewModel
import com.kstorozh.evozhuk.Event
import com.kstorozh.evozhuk.USER_ID_NOT_SET
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class LogInViewModel : BaseViewModel(), KoinComponent {

    private val loginUseCase: LoginUseCase by inject()
    private val getUserUseCase: GetUsersUseCases by inject()
    private val initDeviceUseCases: ManageDeviceUseCases by inject()
    private val applicationScope = CoroutineScope(Dispatchers.Default)
    private val users: MutableLiveData<List<User>> by lazy { MutableLiveData<List<User>>().also {
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

    fun getUserByName(login: String): LiveData<User> {
        return Transformations.map(users, Function<List<User>, User> {
            var user: User? = null
            it.forEach {
                if (it.slackUserName == login) user = it
            }
            return@Function user
        })
    }

    private fun loadUsers() {
        applicationScope.launch {
            val domainRes = getUserUseCase.getUsers()
            domainRes.data?.let { users.postValue(it) }
            domainRes.domainError?.let { errors.postValue(Event(it)) }
        }
    }

    fun tryLogin(name: String, pass: String): LiveData<String> {
        val tryLoginLiveData: MutableLiveData<String> = MutableLiveData<String>()
        applicationScope.launch {
            val domainRes = loginUseCase.loginUser(UserLoginInput(name, pass))
            domainRes.data?.let {
                tryLoginLiveData.postValue(it)
            }
            domainRes.domainError?.let {
                tryLoginLiveData.postValue(USER_ID_NOT_SET)
                errors.postValue(Event(it))
            }
        }
        return tryLoginLiveData
    }

    fun remindPin(user: User): LiveData<Boolean> {
        val remindPinLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
        applicationScope.launch {
            val domainRes = loginUseCase.remindPin(user)
            domainRes.data?.let { remindPinLiveData.postValue(it) }
            domainRes.domainError?.let { errors.postValue(Event(it)) }
        }
        return remindPinLiveData
    }

    fun isDeviceBooked(deviceInputData: DeviceInputData): LiveData<Boolean> {
        val isDeviceBookedLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
        applicationScope.launch {
            val result = initDeviceUseCases.getSession()
            if (result.data != null) isDeviceBookedLiveData.postValue(true) else isDeviceBookedLiveData.postValue(false)
        }
        return isDeviceBookedLiveData
    }
}