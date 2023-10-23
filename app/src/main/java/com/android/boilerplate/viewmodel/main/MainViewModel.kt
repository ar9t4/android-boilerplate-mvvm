package com.android.boilerplate.viewmodel.main

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.android.boilerplate.base.model.data.remote.response.Result
import com.android.boilerplate.base.viewmodel.BaseViewModel
import com.android.boilerplate.model.data.local.database.entities.RandomUser
import com.android.boilerplate.model.data.remote.request.RandomUsersRequest
import com.android.boilerplate.model.repository.main.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * @author Abdul Rahman
 */
@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) :
    BaseViewModel() {

    private val usersLiveData = repository.getRandomUsersLiveData()

    val usersResultLiveData = MediatorLiveData<Result<List<RandomUser>>>()

    init {
        usersResultLiveData.addSource(usersLiveData) {
            usersResultLiveData.value = Result.Success(data = it)
        }
    }

    override fun onCleared() {
        super.onCleared()
        usersResultLiveData.removeSource(usersLiveData)
    }

    fun getUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // use the below line for custom view group based loader handling
                // usersResultLiveData.value = Result.Loading(loading = true)
                showLoader(true)
                repository.getRandomUsers(RandomUsersRequest(10))
                // use the below line for custom view group based loader handling
                // usersResultLiveData.value = Result.Loading(loading = false)
                showLoader(false)
            } catch (exception: Exception) {
                handleException(exception)
                // use the below line for custom view group based error handling
                // usersResultLiveData.value = Result.Failure(exception = exception)
            }
        }
    }

    fun getLatestUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                showLoader(true)
                repository.getLatestRandomUsers(RandomUsersRequest(10))
                showLoader(false)
            } catch (exception: Exception) {
                handleException(exception)
            }
        }
    }
}