package com.kstorozh.evozhuk

import android.app.Application
import com.kstorozh.di.KoinLoader
import com.kstorozh.evozhuk.backDevice.BackDeviceViewModel
import com.kstorozh.evozhuk.notifications.NotificationUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.danlew.android.joda.JodaTimeAndroid
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            KoinLoader.loadKoin()
            modules(appModule)
        }

        NotificationUtil.createNotificationChannel(this)
        JodaTimeAndroid.init(this)
    }

    private val appModule = module {
        single { CoroutineScope(Dispatchers.Default) }
        viewModel { BackDeviceViewModel(get(), get(), get()) }
    }
}