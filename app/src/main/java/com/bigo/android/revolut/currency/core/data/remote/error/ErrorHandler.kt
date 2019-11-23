package com.bigo.android.revolut.currency.core.data.remote.error

import io.reactivex.Observable

interface ErrorHandler {
    fun handle(throwable: Throwable): Throwable
}