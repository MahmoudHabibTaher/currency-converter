package com.bigo.android.revolut.currency.converter.data.remote

import com.google.gson.annotations.SerializedName

class CurrencyRateRemote(
    @SerializedName("base") val base: String,
    @SerializedName("date") val date: String,
    @SerializedName("rates") val rates: Map<String, Double>
)