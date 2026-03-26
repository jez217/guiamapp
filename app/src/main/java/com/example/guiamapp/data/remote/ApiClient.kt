package com.example.guiamapp.data.remote

import com.example.guiamapp.BuildConfig
import com.example.guiamapp.data.local.TokenStore
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiClient {
    fun create(tokenStore: TokenStore): Retrofit {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val ok = OkHttpClient.Builder()
            .addInterceptor(logger)
            .addInterceptor(AuthInterceptor(tokenStore))
            .build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL) // https://10.0.2.2:7110 en debug
            .client(ok)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}