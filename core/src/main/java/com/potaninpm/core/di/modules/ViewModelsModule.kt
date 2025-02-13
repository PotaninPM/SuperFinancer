package com.potaninpm.core.di.modules

import com.potaninpm.feature_home.viewModels.HomeViewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { HomeViewModel(get(), get()) }
}