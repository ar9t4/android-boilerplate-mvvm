package com.android.boilerplate.viewmodel.settings

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

    fun fetchSettingItems() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                showLoader(show = false)
                repository.fetchSettingItems()
                showLoader(show = false)
            } catch (exception: Exception) {
                handleException(exception)
            }
        }
    }

    fun toggleNotification() = repository.toggleNotification()

    val themes = repository.getThemes()

    fun fetchThemes() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                showLoader(show = false)
                repository.fetchThemes()
                showLoader(show = false)
            } catch (exception: Exception) {
                handleException(exception)
            }
        }
    }

    fun updateSelectedTheme() = repository.updateSelectedTheme()

    fun setTheme(selectedTheme: Theme) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                showLoader(show = false)
                repository.setTheme(selectedTheme = selectedTheme)
                showLoader(show = false)
            } catch (exception: Exception) {
                handleException(exception)
            }
        }
    }

    val languages = repository.getLanguages()

    fun fetchLanguages() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                showLoader(show = false)
                repository.fetchLanguages()
                showLoader(show = false)
            } catch (exception: Exception) {
                handleException(exception)
            }
        }
    }

    fun updateSelectedLanguage() = repository.updateSelectedLanguage()

    fun setLanguage(selectedLanguage: Language) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                showLoader(show = false)
                repository.setLanguage(selectedLanguage = selectedLanguage)
                showLoader(show = false)
            } catch (exception: Exception) {
                handleException(exception)
            }
        }
    }
}