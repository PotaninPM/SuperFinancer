package com.potaninpm.finaltour.di

import com.potaninpm.finaltour.di.modules.commonModule
import com.potaninpm.finaltour.di.modules.financeRoomModule
import com.potaninpm.finaltour.di.modules.networkModule
import com.potaninpm.finaltour.di.modules.postsRoomModule
import com.potaninpm.finaltour.di.modules.repositoryModule
import com.potaninpm.finaltour.di.modules.useCaseModule
import com.potaninpm.finaltour.di.modules.viewModelModule


val coreModules = listOf(
    networkModule,
    financeRoomModule,
    postsRoomModule,
    repositoryModule,
    useCaseModule,
    viewModelModule,
    commonModule
)