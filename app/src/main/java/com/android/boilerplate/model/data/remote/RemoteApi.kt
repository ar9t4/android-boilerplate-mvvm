package com.android.boilerplate.model.data.remote

import com.android.boilerplate.model.data.remote.response.RandomUsersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author Abdul Rahman
 */
interface RemoteApi {

    @GET(NetworkEndPoints.GET_USERS)
    suspend fun getUsers(
        @Query("results") results: Int?
    ): Response<RandomUsersResponse>
}