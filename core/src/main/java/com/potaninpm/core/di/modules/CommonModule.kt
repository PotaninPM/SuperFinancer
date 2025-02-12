package com.potaninpm.core.di.modules

import android.content.Context
import com.potaninpm.core.AppDispatchers
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
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

    single {
        androidContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    }
}