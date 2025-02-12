package com.potaninpm.core.di.modules

import com.potaninpm.data.repository.NewsRepositoryImpl
import com.potaninpm.data.repository.TickerRepositoryImpl
import com.potaninpm.domain.repository.NewsRepository
import com.potaninpm.domain.repository.TickerRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<TickerRepository> { TickerRepositoryImpl(get()) }
    single<NewsRepository> { NewsRepositoryImpl(get()) }
}