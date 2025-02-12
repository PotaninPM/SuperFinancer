package com.potaninpm.core.di.modules

import com.potaninpm.core.AppDispatchers
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