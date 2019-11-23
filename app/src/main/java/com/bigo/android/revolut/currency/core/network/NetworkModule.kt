package com.bigo.android.revolut.currency.core.network

import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
object NetworkModule {

    @Provides
    @Singleton
    @JvmStatic
    fun provideRetrofit(
        @Named("BASE_URL") baseUrl: String,
        client: OkHttpClient,
        callAdapterFactory: CallAdapter.Factory,
        converterFactory: Converter.Factory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addCallAdapterFactory(callAdapterFactory)
        .addConverterFactory(converterFactory)
        .build()

    @Provides
    @JvmStatic
    @Named("BASE_URL")
    fun provideBaseUrl(): String = ApiConstants.BASE_URL

    @Provides
    @JvmStatic
    fun provideClient(
        loggingInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @JvmStatic
    fun provideLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor().also { it.level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    @JvmStatic
    fun provideCallAdapter(): CallAdapter.Factory =
        RxJava2CallAdapterFactory.create()

    @Provides
    @JvmStatic
    fun provideConverter(): Converter.Factory =
        GsonConverterFactory.create()
}