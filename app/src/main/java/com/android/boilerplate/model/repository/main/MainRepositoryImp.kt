package com.android.boilerplate.model.repository.main

import androidx.lifecycle.LiveData
import com.android.boilerplate.base.model.repository.BaseRepositoryImp
import com.android.boilerplate.model.data.local.database.daos.RandomUserDao
import com.android.boilerplate.model.data.local.database.entities.RandomUser
import com.android.boilerplate.model.data.local.preference.Preferences
import com.android.boilerplate.model.data.remote.RemoteApi
import com.android.boilerplate.model.data.remote.request.RandomUsersRequest
import javax.inject.Inject

/**
 * @author Abdul Rahman
 */
class MainRepositoryImp @Inject constructor(
    private val userDao: RandomUserDao,
    private val remote: RemoteApi,
    preferences: Preferences
) : MainRepository, BaseRepositoryImp(preferences) {

    private var users = userDao.getUsersLiveData()

    override fun getRandomUsersLiveData(): LiveData<List<RandomUser>> = users

    override suspend fun getRandomUsers(requestRandom: RandomUsersRequest) {
        val users = userDao.getUsers()
        if (users.isEmpty()) {
            val response = remote.getUsers(requestRandom.results)
            response.body()?.results?.let {
                userDao.insert(it)
            }
        }
    }

    override suspend fun getLatestRandomUsers(requestRandom: RandomUsersRequest) {
        val response = remote.getUsers(requestRandom.results)
        response.body()?.results?.let {
            // delete the cached users
            users.value?.let { cachedUsers ->
                userDao.delete(cachedUsers)
            }
            // insert the latest users
            userDao.insert(it)
        }
    }
}