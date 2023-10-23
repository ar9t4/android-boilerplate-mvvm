package com.android.boilerplate.model.repository.settings

import androidx.lifecycle.LiveData
import com.android.boilerplate.model.data.aide.Language
import com.android.boilerplate.model.data.aide.SettingItem
import com.android.boilerplate.model.data.aide.Theme

/**
 * @author Abdul Rahman
 */
interface SettingsRepository {
    fun getSettingItems(): LiveData<List<SettingItem>?>
    suspend fun fetchSettingItems()
    fun toggleNotification()
    fun getThemes(): LiveData<List<Theme>?>
    suspend fun fetchThemes()
    fun updateSelectedTheme()
    suspend fun setTheme(selectedTheme: Theme)
    fun getLanguages(): LiveData<List<Language>?>
    suspend fun fetchLanguages()
    fun updateSelectedLanguage()
    suspend fun setLanguage(selectedLanguage: Language)
}