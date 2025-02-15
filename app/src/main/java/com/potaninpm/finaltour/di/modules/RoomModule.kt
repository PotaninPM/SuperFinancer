package com.potaninpm.finaltour.di.modules

import android.util.Log
import androidx.room.Room
import com.potaninpm.feature_feed.data.local.database.PostDatabase
import com.potaninpm.feature_finances.data.local.database.FinanceDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val financeRoomModule = module {
    single {
        Log.d("KOIN", "Создаем ROOOM")
        Room.databaseBuilder(
            androidApplication(),
            FinanceDatabase::class.java,
            "finance_database.db"
        ).fallbackToDestructiveMigration().build()
    }
    single { FinanceDatabase.getInstance(get()) }

    single {
        get<FinanceDatabase>().goalDao()
    }

    single {
        get<FinanceDatabase>().operationDao()
    }


}

val postsRoomModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            PostDatabase::class.java,
            "posts_database.db"
        ).fallbackToDestructiveMigration().build()
    }

    single {
        get<PostDatabase>().postDao()
    }

    single {
        get<PostDatabase>().commentDao()
    }
}