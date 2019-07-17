package com.kstorozh.evozhuk

import androidx.lifecycle.ViewModel
import com.kstorozh.domain.HandleErrorUseCase

class ErrorViewModel(val errorHandler: HandleErrorUseCase) : ViewModel()