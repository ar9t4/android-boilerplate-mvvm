package com.android.boilerplate.model.repository.main

import androidx.lifecycle.LiveData
import com.android.boilerplate.model.data.local.database.entities.RandomUser
import com.android.boilerplate.model.data.remote.request.RandomUsersRequest

/**
 * @author Abdul Rahman
 */
interface MainRepository {

    fun getRandomUsersLiveData(): LiveData<List<RandomUser>>

    suspend fun getRandomUsers(requestRandom: RandomUsersRequest)

    suspend fun getLatestRandomUsers(requestRandom: RandomUsersRequest)
}