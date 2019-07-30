package com.kstorozh.data.di

import BASE_URL
import DEVICE_INFO_DB_NAME
import androidx.room.Room
import com.google.gson.Gson
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
import com.kstorozh.data.network.AuthInterceptor
import com.kstorozh.data.network.TokenRepository
import com.kstorozh.dataimpl.DeviseRepository
import com.kstorozh.dataimpl.model.out.SlackUser
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder

// The module is marked as override, which means that its content will override any other definition within the application.

val dbModule = module(override = true) {
    single {
        Room.databaseBuilder(androidApplication(), DeviceDatabase::class.java, DEVICE_INFO_DB_NAME)
            .build()
    }
    factory { get<DeviceDatabase>().deviceDao() }
    factory { get<DeviceDatabase>().tokenDao() }
    factory { get<DeviceDatabase>().bookingDao() }

    factory<LocalDataStorage> { LocalDataStorageImpl(get(), get()) }
}

val networkModule = module(override = true) {
    factory { TokenRepository(get()) }
    factory { AuthInterceptor() }
    factory { HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY) }
    factory { provideOkHttpClient(get(), get()) }
    factory { provideDeviceApi(get()) }
    factory { provideUserApi(get()) }
    factory { provideGson() }
    single { provideRetrofit(get(), get()) }
    factory<RemoteData> { RemoteDataImpl(get(), get()) }
}

val repositoryModule = module(override = true) {
    single<DeviseRepository> { DeviceRepositoryImpl(get(), get(), DeviceDataMapper(), get()) }
    single<UserRepository> { UserRepositoryImpl(get(), UserDataMapper()) }
}

private fun provideGson(): Gson {
    return GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .create()
}

private fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {

    return Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson)).build()
}

private fun provideOkHttpClient(authInterceptor: AuthInterceptor, loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {

    return OkHttpClient()
        .newBuilder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()
}
private fun provideDeviceApi(retrofit: Retrofit): DeviceApi = retrofit.create(DeviceApi::class.java)
private fun provideUserApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)
