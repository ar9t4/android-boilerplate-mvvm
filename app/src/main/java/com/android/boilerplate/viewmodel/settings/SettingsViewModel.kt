package com.android.boilerplate.viewmodel.settings

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.android.boilerplate.base.viewmodel.BaseViewModel
import com.android.boilerplate.model.data.aide.Language
import com.android.boilerplate.model.data.aide.Theme
import com.android.boilerplate.model.repository.settings.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Abdul Rahman
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: SettingsRepository) :
    BaseViewModel() {

    val settingItems = repository.getSettingItems()

    fun fetchSettingItems(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                showLoader(show = false)
                repository.fetchSettingItems(context = context)
                showLoader(show = false)
            } catch (exception: Exception) {
                handleException(exception)
            }
        }
    }

    fun toggleNotification(context: Context) = repository.toggleNotification(context = context)

    val themes = repository.getThemes()

    fun fetchThemes(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                showLoader(show = false)
                repository.fetchThemes(context = context)
                showLoader(show = false)
            } catch (exception: Exception) {
                handleException(exception)
            }
        }
    }

    fun updateSelectedTheme(context: Context) = repository.updateSelectedTheme(context = context)

    fun setTheme(context: Context, selectedTheme: Theme) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                showLoader(show = false)
                repository.setTheme(context = context, selectedTheme = selectedTheme)
                showLoader(show = false)
            } catch (exception: Exception) {
                handleException(exception)
            }
        }
    }

    val languages = repository.getLanguages()

    fun fetchLanguages(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                showLoader(show = false)
                repository.fetchLanguages(context = context)
                showLoader(show = false)
            } catch (exception: Exception) {
                handleException(exception)
            }
        }
    }

    fun updateSelectedLanguage(context: Context) = repository.updateSelectedLanguage(context)

    fun setLanguage(context: Context, selectedLanguage: Language) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                showLoader(show = false)
                repository.setLanguage(context = context, selectedLanguage = selectedLanguage)
                showLoader(show = false)
            } catch (exception: Exception) {
                handleException(exception)
            }
        }
    }
}