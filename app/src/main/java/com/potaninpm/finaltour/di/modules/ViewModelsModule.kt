package com.potaninpm.finaltour.di.modules

import com.potaninpm.feature_finances.presentation.viewModels.FinancesViewModel
import com.potaninpm.feature_home.presentation.viewModels.HomeViewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { HomeViewModel(get(), get()) }
    single { FinancesViewModel(get(), get()) }
}