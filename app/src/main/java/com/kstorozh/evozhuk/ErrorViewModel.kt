package com.kstorozh.evozhuk

import androidx.lifecycle.ViewModel
import com.kstorozh.domainimpl.HandleErrorUseCase

class ErrorViewModel(val errorHandler: HandleErrorUseCase) : ViewModel()