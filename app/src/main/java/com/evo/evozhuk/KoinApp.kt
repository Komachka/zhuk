package com.evo.evozhuk

import com.evo.evozhuk.backDevice.BackDeviceViewModel
import com.evo.evozhuk.calendar.CalendarViewModel
import com.evo.evozhuk.calendar_day.DayViewModel
import com.evo.evozhuk.chooseTime.ChooseTimeViewModel
import com.evo.evozhuk.chooseTime.SpecificTimeAndDateViewModel
import com.evo.evozhuk.home.HomeViewModel
import com.evo.evozhuk.info.InfoViewModel
import com.evo.evozhuk.login.LogInViewModel
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