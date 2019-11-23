package com.bigo.android.revolut.currency.core.data.remote.error

import com.bigo.android.revolut.currency.core.errors.ConnectivityException
import java.io.IOException

class RemoteErrorHandler : ErrorHandler {
    override fun handle(throwable: Throwable): Throwable =
        when (throwable) {
            is IOException -> ConnectivityException()
            else -> throwable
        }
}