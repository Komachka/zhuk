package com.kstorozh.domain.di


import androidx.lifecycle.MutableLiveData

import com.kstorozh.data.repository.*


import com.kstorozh.dataimpl.DeviseRepository
import com.kstorozh.dataimpl.MyError
import com.kstorozh.dataimpl.model.out.SlackUser

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


// The module is marked as override, which means that its content will override any other definition within the application.

val useCaseModules = module(override = true) {

    factory { provideOkHttpClient(get()) }
    factory { provideDeviceApi(get()) }
    factory { provideUserApi(get()) }
    single { provideRetrofit(get()) }
    factory<RemoteData> { RemoteDataImpl(get(), get()) }
}

val dbModule = module(override = true) {
    single {
        DeviceDatabase.getDatabase(androidContext())
    }
    factory { get<DeviceDatabase>().deviceDao() }
    factory<LocalDataStorage> { LocalDataStorageImpl(get()) }
}

val repositoryModule = module(override = true) {
    single<DeviseRepository> { DeviceRepositoryImpl(get(), get(), DeviceDataMapper(), MutableLiveData(), get()) }
    single<UserRepository> { UserRepositoryImpl(get(), UserDataMapper(), MutableLiveData<MyError>(), MutableLiveData<List<SlackUser>>()) }
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
}
fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
    return OkHttpClient().newBuilder().addInterceptor(authInterceptor).build()
}
private fun provideDeviceApi(retrofit: Retrofit): DeviceApi = retrofit.create(DeviceApi::class.java)
private fun provideUserApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)
