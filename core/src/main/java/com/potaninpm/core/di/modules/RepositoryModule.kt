package com.potaninpm.core.di.modules

import com.potaninpm.data.repository.home.NewsRepositoryImpl
import com.potaninpm.data.repository.home.TickerRepositoryImpl
import com.potaninpm.domain.repository.finances.NewsRepository
import com.potaninpm.domain.repository.finances.TickerRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<TickerRepository> { TickerRepositoryImpl(get()) }
    single<NewsRepository> { NewsRepositoryImpl(get()) }
}