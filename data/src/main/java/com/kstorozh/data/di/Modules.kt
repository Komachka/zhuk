package com.kstorozh.data.di

import BASE_URL
import DEVICE_INFO_DB_NAME
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.kstorozh.data.database.DeviceDatabase
import com.kstorozh.data.database.LocalDataStorage
import com.kstorozh.data.database.LocalDataStorageImpl
import com.kstorozh.data.network.DeviceApi
import com.kstorozh.data.network.RemoteData
import com.kstorozh.data.network.RemoteDataImpl
import com.kstorozh.data.network.UserApi
import com.kstorozh.data.repository.*
import com.kstorozh.data.repository.DeviceDataMapper
import com.kstorozh.data.repository.DeviceRepositoryImpl
import com.kstorozh.data.repository.UserDataMapper
import com.kstorozh.data.repository.UserRepositoryImpl
import com.kstorozh.data.utils.AuthInterceptor
import com.kstorozh.data.utils.TokenRepository
import com.kstorozh.dataimpl.DeviseRepository
import com.kstorozh.dataimpl.MyError
import com.kstorozh.dataimpl.model.out.SlackUser
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder

// The module is marked as override, which means that its content will override any other definition within the application.

val networkModule = module(override = true) {
    factory { TokenRepository() }
    factory { AuthInterceptor(get()) }
    factory { HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY) }
    factory { provideOkHttpClient(get(), get()) }
    factory { provideDeviceApi(get()) }
    factory { provideUserApi(get()) }
    single { provideRetrofit(get()) }
    factory<RemoteData> { RemoteDataImpl(get(), get()) }
}

val dbModule = module(override = true) {
    single {
        Room.databaseBuilder(androidApplication(), DeviceDatabase::class.java, DEVICE_INFO_DB_NAME)
            .build()
    }
    factory { get<DeviceDatabase>().deviceDao() }
    factory<LocalDataStorage> { LocalDataStorageImpl(get()) }
}

val repositoryModule = module(override = true) {
    single<DeviseRepository> { DeviceRepositoryImpl(get(), get(), DeviceDataMapper(), MutableLiveData(), get()) }
    single<UserRepository> { UserRepositoryImpl(get(), UserDataMapper(), MutableLiveData<MyError>(), MutableLiveData<List<SlackUser>>()) }
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {

    val gson = GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .create()

    return Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson)).build()
}

fun provideOkHttpClient(authInterceptor: AuthInterceptor, loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {

    return OkHttpClient()
        .newBuilder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()
}
private fun provideDeviceApi(retrofit: Retrofit): DeviceApi = retrofit.create(DeviceApi::class.java)
private fun provideUserApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)
