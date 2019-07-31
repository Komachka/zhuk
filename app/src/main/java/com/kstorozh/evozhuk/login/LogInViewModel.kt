package com.kstorozh.evozhuk.login

import androidx.arch.core.util.Function
import androidx.lifecycle.*
import com.kstorozh.domainapi.LoginUseCase
import com.kstorozh.domainapi.ManageDeviceUseCases
import com.kstorozh.domainapi.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class LogInViewModel : ViewModel(), KoinComponent {

    private val loginUseCase: LoginUseCase by inject()
    private val getUserUseCase: GetUsersUseCases by inject()
    private val initDeviceUseCases: ManageDeviceUseCases by inject()

    private val applicationScope = CoroutineScope(Dispatchers.Default)
    private val users: MutableLiveData<List<User>> by lazy { MutableLiveData<List<User>>().also {
        loadUsers()
    } }

    private val tryLoginViewModel: MutableLiveData<String> = MutableLiveData<String>()
    private val remindPinViewModel: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val isDeviceBookedViewModel: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val errorViewModel: MutableLiveData<DomainErrors> = MutableLiveData<DomainErrors>()

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
            domainRes.domainError?.let { errorViewModel.postValue(it) }
        }
    }

    fun tryLogin(name: String, pass: String): LiveData<String> {
        applicationScope.launch {
            val domainRes = loginUseCase.loginUser(UserLoginInput(name, pass))
            domainRes.data?.let { tryLoginViewModel.postValue(it) }
            domainRes.domainError?.let { errorViewModel.postValue(it) }
        }
        return tryLoginViewModel
    }

    fun remindPin(user: User): LiveData<Boolean> {

        applicationScope.launch {
            val domainRes = loginUseCase.remindPin(user)
            domainRes.data?.let { remindPinViewModel.postValue(it) }
            domainRes.domainError?.let { errorViewModel.postValue(it) }
        }
        return remindPinViewModel
    }

    fun isDeviceBooked(deviceInputData: DeviceInputData): LiveData<Boolean> {

        applicationScope.launch {
            val result = initDeviceUseCases.getSession()
            if (result.data != null) isDeviceBookedViewModel.postValue(true) else isDeviceBookedViewModel.postValue(false)
        }
        return isDeviceBookedViewModel
    }
}