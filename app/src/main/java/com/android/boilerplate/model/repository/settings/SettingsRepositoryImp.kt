package com.android.boilerplate.model.repository.settings

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.boilerplate.R
import com.android.boilerplate.aide.workers.PeriodicWorkerUtils
import com.android.boilerplate.model.data.aide.Language
import com.android.boilerplate.model.data.aide.SettingItem
import com.android.boilerplate.model.data.aide.Theme
import com.android.boilerplate.model.data.local.preference.Preferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * @author Abdul Rahman
 */
class SettingsRepositoryImp @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val preferences: Preferences
) : SettingsRepository {

    private val themesData: MutableLiveData<List<Theme>?> = MutableLiveData(null)
    private val languagesData: MutableLiveData<List<Language>?> = MutableLiveData(null)
    private val settingItemsData: MutableLiveData<List<SettingItem>?> = MutableLiveData(null)

    override fun getSettingItems(): LiveData<List<SettingItem>?> = settingItemsData

    override suspend fun fetchSettingItems() {
        if (settingItemsData.value == null) {
            val items = mutableListOf<SettingItem>()
            context.apply {
                items.add(
                    SettingItem(
                        id = 1,
                        key = getString(R.string.notifications),
                        value = getActiveNotificationStateName()
                    )
                )
                items.add(
                    SettingItem(
                        id = 2,
                        key = getString(R.string.theme),
                        value = ""
                    )
                )
                items.add(
                    SettingItem(
                        id = 3,
                        key = getString(R.string.language),
                        value = ""
                    )
                )
            }
            settingItemsData.postValue(items)
            // fetch themes and mark selected theme
            getSelectedThemeName()
            // fetch languages and mark selected language
            getSelectedLanguageName()
        }
    }

    private suspend fun getSelectedThemeName(): String {
        // fetch themes
        if (themesData.value == null) {
            fetchThemes()
        }
        val selectedThemeName = context.getString(R.string.system_default)
        themesData.value?.let { themes ->
            themes.find { it.selected }?.let { return it.name } ?: return selectedThemeName
        }
        return selectedThemeName
    }

    private suspend fun getSelectedLanguageName(): String {
        // fetch languages
        if (languagesData.value == null) {
            fetchLanguages()
        }
        val selectedLanguageName = context.getString(R.string.system_default)
        languagesData.value?.let { languages ->
            languages.find { it.selected }?.let { return it.name } ?: return selectedLanguageName
        }
        return selectedLanguageName
    }

    override fun toggleNotification() {
        val notificationState = preferences.getBoolean(Preferences.KEY_NOTIFICATION)
        if (notificationState) {
            // turn off notifications
            PeriodicWorkerUtils.cancelPeriodicWorker(context)
        } else {
            // turn on notifications
            PeriodicWorkerUtils.createPeriodicWorker(context)
        }
        // toggle notification state
        preferences.setBoolean(Preferences.KEY_NOTIFICATION, !notificationState)
        // update notification settings data
        updateNotificationSettings()
    }

    private fun updateNotificationSettings() {
        settingItemsData.value?.let { settingItems ->
            val item = settingItems.find { it.key == context.getString(R.string.notifications) }
            item?.let {
                it.value = getActiveNotificationStateName()
            }
            settingItemsData.postValue(settingItems)
        }
    }

    private fun getActiveNotificationStateName(): String {
        val value = preferences.getBoolean(Preferences.KEY_NOTIFICATION)
        return if (value) context.getString(R.string.on) else context.getString(R.string.off)
    }

    override fun getThemes(): LiveData<List<Theme>?> = themesData

    override suspend fun fetchThemes() {
        if (themesData.value == null) {
            val selectedThemeId = when (preferences.getInt(Preferences.KEY_THEME)) {
                AppCompatDelegate.MODE_NIGHT_YES -> 2
                AppCompatDelegate.MODE_NIGHT_NO -> 1
                else -> 0
            }
            val themes = mutableListOf<Theme>()
            themes.apply {
                add(Theme(id = 0, context.getString(R.string.system_default)))
                add(Theme(id = 1, context.getString(R.string.light)))
                add(Theme(id = 2, context.getString(R.string.dark)))
            }
            themes.find { it.id == selectedThemeId }?.let {
                it.selected = true
            }
            themesData.postValue(themes)
        }
    }

    override fun updateSelectedTheme() {
        themesData.value?.let { themes ->
            themes.find { it.selected }?.let { selectedTheme ->
                updateThemeSettings(selectedTheme = selectedTheme)
            }
        }
    }

    private fun updateThemeSettings(selectedTheme: Theme) {
        // update selected theme in settings item
        settingItemsData.value?.let { settingItems ->
            settingItems.find { it.key == context.getString(R.string.theme) }?.let {
                it.value = selectedTheme.name
            }
            settingItemsData.postValue(settingItems)
        }
    }

    override suspend fun setTheme(selectedTheme: Theme) {
        themesData.value?.let { themes ->
            themes.find { it.selected }?.let { currentTheme ->
                if (currentTheme.id != selectedTheme.id) {
                    // save the selected theme in preferences
                    preferences.setInt(Preferences.KEY_THEME, selectedTheme.id)
                    // update selected theme in themes
                    themes.forEach { theme ->
                        theme.selected = theme.id == selectedTheme.id
                    }
                    themesData.postValue(themes)
                    // update selected theme in settings item
                    updateThemeSettings(selectedTheme = selectedTheme)
                }
            }
        }
    }

    override fun getLanguages(): LiveData<List<Language>?> = languagesData

    override suspend fun fetchLanguages() {
        if (languagesData.value == null) {
            // parse json file
            val json = context.assets.open("languages.json").bufferedReader()
                .use { it.readText() }
            val type = object : TypeToken<List<Language>>() {}.type
            val languages = Gson().fromJson<List<Language>>(json, type)
            if (!languages.isNullOrEmpty()) {
                val lang = preferences.getString(Preferences.KEY_LANG)
                if (lang == null || lang == Preferences.KEY_DEFAULT) {
                    languages[0].selected = true
                } else {
                    languages.forEach { item ->
                        item.selected = item.lang == lang
                    }
                }
            }
            languagesData.postValue(languages)
        }
    }

    override fun updateSelectedLanguage() {
        languagesData.value?.let { languages ->
            languages.find { it.selected }?.let { selectedLanguage ->
                updateLanguageSettings(selectedLanguage = selectedLanguage)
            }
        }
    }

    private fun updateLanguageSettings(selectedLanguage: Language) {
        // update selected language in settings item
        settingItemsData.value?.let { settingItems ->
            settingItems.find { it.key == context.getString(R.string.language) }?.let {
                it.value = selectedLanguage.name
            }
            settingItemsData.postValue(settingItems)
        }
    }

    override suspend fun setLanguage(selectedLanguage: Language) {
        languagesData.value?.let { languages ->
            languages.find { it.selected }?.let { currentLanguage ->
                if (currentLanguage.id != selectedLanguage.id) {
                    // save the selected language in preferences
                    preferences.setString(Preferences.KEY_LANG, selectedLanguage.lang)
                    // update selected language in languages
                    languages.forEach { language ->
                        language.selected = language.id == selectedLanguage.id
                    }
                    languagesData.postValue(languages)
                    // update selected language in settings item
                    updateLanguageSettings(selectedLanguage = selectedLanguage)
                }
            }
        }
    }
}