package com.potaninpm.finaltour.di.modules

import com.potaninpm.finaltour.AppDispatchers
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val commonModule = module {

    single<AppDispatchers> {
        AppDispatchers(
            ui = Dispatchers.Main,
            io = Dispatchers.IO,
            default = Dispatchers.Default,
            unconfined = Dispatchers.Unconfined
        )
    }
}