package com.bigo.android.revolut.currency.converter.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {
    @GET("latest")
    suspend fun getRates(
        @Query("base") base: String
    ): CurrencyRateRemote
}