package com.potaninpm.finaltour.di.modules

import com.potaninpm.feature_feed.presentation.viewModels.PostsViewModel
import com.potaninpm.feature_finances.presentation.viewModels.FinancesViewModel
import com.potaninpm.feature_home.presentation.viewModels.HomeViewModel
import com.potaninpm.feature_home.presentation.viewModels.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { HomeViewModel(get(), get()) }
    single { FinancesViewModel(get(), get()) }
    viewModel { SearchViewModel(get(), get()) }
    single { PostsViewModel(get()) }
}