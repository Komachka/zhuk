package com.kstorozh.evozhuk

import androidx.lifecycle.ViewModel
import com.kstorozh.domainapi.HandleErrorUseCase


class ErrorViewModel(val errorHandler: HandleErrorUseCase) : ViewModel()