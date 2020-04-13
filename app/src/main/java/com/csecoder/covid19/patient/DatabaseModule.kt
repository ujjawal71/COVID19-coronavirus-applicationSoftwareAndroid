package com.csecoder.covid19.patient

import android.content.Context
import androidx.room.Room
import com.csecoder.covid19.data.database.AppDatabase

import org.koin.dsl.module


val databaseModule = module {
    single { provideAppDatabase(get()) }
    single { provideDummyDao(get()) }
    single { provideCoronaDao1(get()) }
    single { provideCoronaDao2(get()) }
    single { provideCoronaDao3(get()) }
    single { provideCoronaDao4(get()) }
}

const val DATABASE_NAME = "app_db"

private fun provideAppDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        DATABASE_NAME
    ).fallbackToDestructiveMigration().build()
}

private fun provideDummyDao(database: AppDatabase) = database.dummyDao()
private fun provideCoronaDao1(database: AppDatabase) = database.coronaDao1()
private fun provideCoronaDao2(database: AppDatabase) = database.coronaDao2()
private fun provideCoronaDao3(database: AppDatabase) = database.coronaDao3()
private fun provideCoronaDao4(database: AppDatabase) = database.coronaDao4()