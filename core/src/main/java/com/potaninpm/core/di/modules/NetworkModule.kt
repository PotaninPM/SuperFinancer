package com.potaninpm.core.di.modules

import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val networkModule = module {
    single(named("authRetrofit")) {
        Retrofit.Builder()
            .baseUrl("https://*.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //single { get<Retrofit>(named("authRetrofit")).create(AuthApi::class.java) }
}