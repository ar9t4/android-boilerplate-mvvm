package com.android.boilerplate.di

import com.android.boilerplate.BuildConfig
import com.android.boilerplate.model.data.local.preference.Preferences
import com.android.boilerplate.model.data.remote.NetworkParams
import com.android.boilerplate.model.data.remote.RemoteApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * @author Abdul Rahman
 */
@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideOkHttpClient(preferences: Preferences): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.apply {
            level = if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        }
        val builder = OkHttpClient.Builder()
            .connectTimeout(NetworkParams.CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(NetworkParams.WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(NetworkParams.READ_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(
                Interceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader(NetworkParams.CONTENT_TYPE, NetworkParams.APPLICATION_JSON)
                        .addHeader(NetworkParams.ACCEPT, NetworkParams.APPLICATION_JSON)
                    preferences.getSignInUser()?.accessToken?.let {
                        request.addHeader(
                            NetworkParams.AUTHORIZATION,
                            NetworkParams.BEARER.plus(it)
                        )
                    }
                    chain.proceed(request.build())
                }
            )
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideRemoteApi(client: OkHttpClient): RemoteApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(RemoteApi::class.java)
    }
}