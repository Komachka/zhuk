package com.kstorozh.evozhuk

import com.kstorozh.evozhuk.backDevice.BackDeviceViewModel
import com.kstorozh.evozhuk.calendar.CalendarViewModel
import com.kstorozh.evozhuk.calendar_day.DayViewModel
import com.kstorozh.evozhuk.chooseTime.ChooseTimeViewModel
import com.kstorozh.evozhuk.chooseTime.SpecificTimeAndDateViewModel
import com.kstorozh.evozhuk.home.HomeViewModel
import com.kstorozh.evozhuk.info.InfoViewModel
import com.kstorozh.evozhuk.login.LogInViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appModule = module {
    single { CoroutineScope(Dispatchers.Default) }
    viewModel { BackDeviceViewModel(get(), get(), get()) }
    viewModel { CalendarViewModel(get(), get(), get()) }
    viewModel { DayViewModel(get(), get(), get()) }
    viewModel { ChooseTimeViewModel(get(), get()) }
    viewModel { SpecificTimeAndDateViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { InfoViewModel(get(), get(), get()) }
    viewModel { LogInViewModel(get(), get(), get(), get(), get()) }
    viewModel { BaseViewModel(get(), get()) }
}