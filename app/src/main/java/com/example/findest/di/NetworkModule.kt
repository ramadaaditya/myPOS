package com.example.findest.di

import com.example.findest.utils.NetworkChecker
import com.example.findest.utils.NetworkCheckerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {
    @Binds
    @Singleton
    abstract fun bindNetworkChecker(
        impl: NetworkCheckerImpl
    ): NetworkChecker
}