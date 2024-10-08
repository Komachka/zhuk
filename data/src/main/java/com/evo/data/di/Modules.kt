package com.evo.data.di

import BASE_URL
import DEVICE_INFO_DB_NAME
import androidx.room.Room
import com.google.gson.Gson
import com.evo.data.database.DeviceDatabase
import com.evo.data.database.LocalDataStorage
import com.evo.data.database.LocalDataStorageImpl
import com.evo.data.repository.*
import com.evo.data.repository.DeviceDataMapper
import com.evo.data.repository.DeviceRepositoryImpl
import com.evo.data.repository.UserDataMapper
import com.evo.data.repository.UserRepositoryImpl
import com.evo.dataimpl.DeviseRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import com.evo.data.BookingsCache
import com.evo.data.network.AuthInterceptor
import com.evo.data.network.CalendarApi
import com.evo.data.network.DeviceApi
import com.evo.data.network.RemoteData
import com.evo.data.network.RemoteDataImpl
import com.evo.data.network.TokenRepository
import com.evo.data.network.UserApi
import com.evo.dataimpl.CalendarRepository

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
    factory { provideCalendarApi(get()) }
    factory { provideGson() }
    single { provideRetrofit(get(), get()) }
    factory<RemoteData> { RemoteDataImpl(get(), get(), get()) }
}

val repositoryModule = module(override = true) {
    single<DeviseRepository> { DeviceRepositoryImpl(get(), get(), DeviceDataMapper(), get()) }
    single<UserRepository> { UserRepositoryImpl(get(), UserDataMapper()) }
    single<BookingsCache> { BookingsCache() }
    single<CalendarRepository> { CalendarRepositoryImpl(get(), BookingDataMapper(), get(), get()) }
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
private fun provideCalendarApi(retrofit: Retrofit) = retrofit.create(CalendarApi::class.java)