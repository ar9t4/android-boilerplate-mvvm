package com.android.boilerplate.model.data.local.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.boilerplate.model.data.local.database.entities.RandomUser

/**
 * @author Abdul Rahman
 */
@Dao
interface RandomUserDao {

    @Query("select * from randomuser")
    fun getUsersLiveData(): LiveData<List<RandomUser>>

    @Query("select * from randomuser")
    suspend fun getUsers(): List<RandomUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(users: List<RandomUser>)

    @Delete
    suspend fun delete(users: List<RandomUser>)
}