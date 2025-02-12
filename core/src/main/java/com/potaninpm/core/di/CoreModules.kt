package com.potaninpm.core.di

import com.potaninpm.core.di.modules.commonModule
import com.potaninpm.core.di.modules.networkModule
import com.potaninpm.core.di.modules.repositoryModule
import com.potaninpm.core.di.modules.roomModule
import com.potaninpm.core.di.modules.useCaseModule
import com.potaninpm.core.di.modules.viewModelModule

val coreModules = listOf(
    viewModelModule,
    networkModule,
    repositoryModule,
    useCaseModule,
    roomModule,
    commonModule
)