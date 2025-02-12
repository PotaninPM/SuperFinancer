package com.potaninpm.core.di.modules

import com.potaninpm.feature_home.presentation.viewModels.HomeViewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { HomeViewModel(get(), get()) }
}