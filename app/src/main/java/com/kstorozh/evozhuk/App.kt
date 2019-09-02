package com.kstorozh.evozhuk

import android.app.Application
import com.kstorozh.di.KoinLoader
import com.kstorozh.evozhuk.notifications.NotificationUtil
import net.danlew.android.joda.JodaTimeAndroid
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

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
}