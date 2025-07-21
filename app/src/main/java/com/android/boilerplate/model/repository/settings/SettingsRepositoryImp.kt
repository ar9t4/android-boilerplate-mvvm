package com.android.boilerplate.model.repository.settings

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.boilerplate.R
import com.android.boilerplate.aide.workers.PeriodicWorkerUtils
import com.android.boilerplate.base.model.repository.BaseRepositoryImp
import com.android.boilerplate.model.data.aide.Language
import com.android.boilerplate.model.data.aide.SettingItem
import com.android.boilerplate.model.data.aide.Theme
import com.android.boilerplate.model.data.local.preference.Preferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * @author Abdul Rahman
 */
class SettingsRepositoryImp @Inject constructor(private val preferences: Preferences) :
    SettingsRepository, BaseRepositoryImp(preferences = preferences) {

    private val themeItems = mutableListOf<Theme>()
    private val themeItemsData: MutableLiveData<List<Theme>?> = MutableLiveData(null)

    private val languagesData: MutableLiveData<List<Language>?> = MutableLiveData(null)

    private val settingItems = mutableListOf<SettingItem>()
    private val settingItemsData: MutableLiveData<List<SettingItem>?> = MutableLiveData(null)

    override fun getSettingItems(): LiveData<List<SettingItem>?> = settingItemsData

    override suspend fun fetchSettingItems(context: Context) {
        if (settingItemsData.value == null || hasLocaleChanged()) {
            settingItems.apply {
                // remove previous items
                clear()
                // add new items
                context.apply {
                    add(
                        SettingItem(
                            id = 1,
                            key = getString(R.string.notifications),
                            value = getActiveNotificationStateName(context = context)
                        )
                    )
                    add(
                        SettingItem(
                            id = 2,
                            key = getString(R.string.theme),
                            value = getSelectedThemeName(context = context)
                        )
                    )
                    add(
                        SettingItem(
                            id = 3,
                            key = getString(R.string.language),
                            value = getSelectedLanguageName(context = context)
                        )
                    )
                }
                delay(50)
                settingItemsData.postValue(settingItems)
            }
        }
    }

    private suspend fun getSelectedThemeName(context: Context): String {
        // fetch themes
        val themesData = fetchThemes(context = context)
        val selectedThemeName = context.getString(R.string.system_default)
        themesData.find { it.selected }?.let { return it.name } ?: return selectedThemeName
    }

    private suspend fun getSelectedLanguageName(context: Context): String {
        // fetch languages
        val languagesData = fetchLanguages(context = context)
        val selectedLanguageName = context.getString(R.string.english)
        languagesData?.let { languages ->
            languages.find { it.selected }?.let { return it.name } ?: return selectedLanguageName
        } ?: return selectedLanguageName
    }

    override fun toggleNotification(context: Context) {
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
        updateNotificationSettings(context = context)
    }

    private fun updateNotificationSettings(context: Context) {
        settingItemsData.value?.let { settingItems ->
            val item = settingItems.find { it.key == context.getString(R.string.notifications) }
            item?.let {
                it.value = getActiveNotificationStateName(context = context)
            }
            settingItemsData.postValue(settingItems)
        }
    }

    private fun getActiveNotificationStateName(context: Context): String {
        val value = preferences.getBoolean(Preferences.KEY_NOTIFICATION)
        return if (value) context.getString(R.string.on) else context.getString(R.string.off)
    }

    override fun getThemes(): LiveData<List<Theme>?> = themeItemsData

    override suspend fun fetchThemes(context: Context): List<Theme> {
        val selectedThemeId = when (preferences.getInt(Preferences.KEY_THEME)) {
            AppCompatDelegate.MODE_NIGHT_YES -> 2
            AppCompatDelegate.MODE_NIGHT_NO -> 1
            else -> 0
        }
        themeItems.apply {
            // remove previous items
            clear()
            // add new items
            context.apply {
                add(Theme(id = 0, getString(R.string.system_default)))
                add(Theme(id = 1, getString(R.string.light)))
                add(Theme(id = 2, getString(R.string.dark)))
            }
        }
        themeItems.find { it.id == selectedThemeId }?.let {
            it.selected = true
        }
        themeItemsData.postValue(themeItems)
        return themeItems
    }

    override fun updateSelectedTheme(context: Context) {
        themeItemsData.value?.let { themes ->
            themes.find { it.selected }?.let { selectedTheme ->
                updateThemeSettings(context = context, selectedTheme = selectedTheme)
            }
        }
    }

    private fun updateThemeSettings(context: Context, selectedTheme: Theme) {
        // update selected theme in settings item
        settingItemsData.value?.let { settingItems ->
            val updatedItems = settingItems.map { item ->
                if (item.key == context.getString(R.string.theme)) {
                    item.copy(value = selectedTheme.name)
                } else {
                    item
                }
            }
            settingItemsData.postValue(updatedItems)
        }
    }

    override suspend fun setTheme(context: Context, selectedTheme: Theme) {
        themeItemsData.value?.let { themes ->
            themes.find { it.selected }?.let { currentTheme ->
                if (currentTheme.id != selectedTheme.id) {
                    // save the selected theme in preferences
                    preferences.setInt(
                        Preferences.KEY_THEME,
                        if (selectedTheme.id == 0) -1 else selectedTheme.id
                    )
                    // update selected theme in themes
                    themes.forEach { theme ->
                        theme.selected = theme.id == selectedTheme.id
                    }
                    themeItemsData.postValue(themes)
                    // update selected theme in settings item
                    updateThemeSettings(context = context, selectedTheme = selectedTheme)
                }
            }
        }
    }

    override fun getLanguages(): LiveData<List<Language>?> = languagesData

    override suspend fun fetchLanguages(context: Context): List<Language>? {
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
            return languages
        }
        return languagesData.value
    }

    override fun updateSelectedLanguage(context: Context) {
        languagesData.value?.let { languages ->
            languages.find { it.selected }?.let { selectedLanguage ->
                updateLanguageSettings(context = context, selectedLanguage = selectedLanguage)
            }
        }
    }

    private fun updateLanguageSettings(context: Context, selectedLanguage: Language) {
        // update selected language in settings item
        settingItemsData.value?.let { settingItems ->
            val updatedItems = settingItems.map { item ->
                if (item.key == context.getString(R.string.language)) {
                    item.copy(value = selectedLanguage.name)
                } else {
                    item
                }
            }
            settingItemsData.postValue(updatedItems)
        }
    }

    override suspend fun setLanguage(context: Context, selectedLanguage: Language) {
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
                    updateLanguageSettings(context = context, selectedLanguage = selectedLanguage)
                }
            }
        }
    }
}