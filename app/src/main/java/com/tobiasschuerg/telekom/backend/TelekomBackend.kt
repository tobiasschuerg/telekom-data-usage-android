package com.tobiasschuerg.telekom.backend

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object TelekomBackend {

    val instance: TelekomApi by lazy {
        val retrofit = Retrofit.Builder()
                .baseUrl(TelekomApi.API_BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(
                        OkHttpClient.Builder()
                                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                                .addInterceptor { chain ->
                                    val originalRequest = chain.request()
                                    val requestWithUserAgent = originalRequest.newBuilder()
                                            .header("User-Agent", "Mozilla/4.0")
                                            .build()
                                    chain.proceed(requestWithUserAgent)
                                }
                                .build()
                )
                .build()

        retrofit.create(TelekomApi::class.java)
    }

}