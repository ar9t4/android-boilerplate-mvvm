package com.android.boilerplate.di.settings

import com.android.boilerplate.model.repository.settings.SettingsRepository
import com.android.boilerplate.model.repository.settings.SettingsRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * @author Abdul Rahman
 */
@Module
@InstallIn(ViewModelComponent::class)
abstract class SettingsModule {

    @Binds
    abstract fun bindSettingsRepository(settingsRepositoryImp: SettingsRepositoryImp)
            : SettingsRepository
}