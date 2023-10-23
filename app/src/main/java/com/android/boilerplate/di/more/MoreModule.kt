package com.android.boilerplate.di.more

import com.android.boilerplate.model.repository.more.MoreRepository
import com.android.boilerplate.model.repository.more.MoreRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * @author Abdul Rahman
 */
@Module
@InstallIn(ViewModelComponent::class)
abstract class MoreModule {

    @Binds
    abstract fun bindMoreRepository(repositoryImp: MoreRepositoryImp)
            : MoreRepository
}