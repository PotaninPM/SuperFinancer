package com.potaninpm.finaltour.di.modules

import com.potaninpm.feature_home.data.repository.NewsRepositoryImpl
import com.potaninpm.feature_home.data.repository.TickerRepositoryImpl
import com.potaninpm.feature_home.domain.repository.NewsRepository
import com.potaninpm.feature_home.domain.repository.TickerRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<TickerRepository> { TickerRepositoryImpl(get()) }
    single<NewsRepository> { NewsRepositoryImpl(get()) }
}