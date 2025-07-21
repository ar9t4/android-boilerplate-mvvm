package com.android.boilerplate.model.repository.settings

import android.content.Context
import androidx.lifecycle.LiveData
import com.android.boilerplate.model.data.aide.Language
import com.android.boilerplate.model.data.aide.SettingItem
import com.android.boilerplate.model.data.aide.Theme

/**
 * @author Abdul Rahman
 */
interface SettingsRepository {
    fun getSettingItems(): LiveData<List<SettingItem>?>
    suspend fun fetchSettingItems(context: Context)
    fun toggleNotification(context: Context)
    fun getThemes(): LiveData<List<Theme>?>
    suspend fun fetchThemes(context: Context): List<Theme>
    fun updateSelectedTheme(context: Context)
    suspend fun setTheme(context: Context, selectedTheme: Theme)
    fun getLanguages(): LiveData<List<Language>?>
    suspend fun fetchLanguages(context: Context): List<Language>?
    fun updateSelectedLanguage(context: Context)
    suspend fun setLanguage(context: Context, selectedLanguage: Language)
}