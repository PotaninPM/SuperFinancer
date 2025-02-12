package com.potaninpm.core.di.modules

import com.potaninpm.data.remote.api.FinnhubApi
import com.potaninpm.data.remote.api.NYTimesApi
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single(named("finnhubRetrofit")) {
        Retrofit.Builder()
            .baseUrl("https://finnhub.io/api/v1/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single { get<Retrofit>(named("finnhubRetrofit")).create(FinnhubApi::class.java) }

    single(named("nytimesRetrofit")) {
        Retrofit.Builder()
            .baseUrl("https://api.nytimes.com/svc/search/v2/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<NYTimesApi> { get<Retrofit>(named("nytimesRetrofit")).create(NYTimesApi::class.java) }

}