package com.android.boilerplate.di.main

import com.android.boilerplate.model.repository.main.MainRepository
import com.android.boilerplate.model.repository.main.MainRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author Abdul Rahman
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class MainModule {

    @Singleton
    @Binds
    abstract fun bindMainRepository(repositoryImp: MainRepositoryImp): MainRepository
}