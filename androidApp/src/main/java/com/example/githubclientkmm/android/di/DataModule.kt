package com.example.githubclientkmm.android.di

import com.example.githubclientkmm.DI
import com.example.githubclientkmm.data.AppRepository
import com.example.githubclientkmm.data.KeyValueStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideKeyValueStorage(): KeyValueStorage = DI.storage

    @Singleton
    @Provides
    fun provideAppRepository(): AppRepository = DI.appRepo
}