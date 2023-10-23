package com.android.boilerplate.di.feedback

import com.android.boilerplate.model.repository.feedback.FeedbackRepository
import com.android.boilerplate.model.repository.feedback.FeedbackRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * @author Abdul Rahman
 */
@Module
@InstallIn(ViewModelComponent::class)
abstract class FeedbackModule {

    @Binds
    abstract fun bindFeedbackRepository(repositoryImp: FeedbackRepositoryImp)
            : FeedbackRepository
}