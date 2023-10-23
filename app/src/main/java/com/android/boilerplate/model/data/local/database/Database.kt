package com.android.boilerplate.model.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.boilerplate.model.data.local.database.daos.RandomUserDao
import com.android.boilerplate.model.data.local.database.entities.RandomUser

/**
 * @author Abdul Rahman
 */
@Database(entities = [RandomUser::class], exportSchema = false, version = 1)
abstract class Database : RoomDatabase() {

    abstract fun randomUserDao(): RandomUserDao
}