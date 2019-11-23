package com.bigo.android.revolut.currency.core.data.remote

import com.bigo.android.revolut.currency.core.data.remote.error.ErrorHandler
import io.reactivex.Observable

abstract class RemoteDataSource(
    private val errorHandler: ErrorHandler
) {
    protected fun <T> call(
        observable: Observable<T>,
        onError: (Throwable) -> Observable<T>? = { null }
    ): Observable<T> = observable.onErrorResumeNext { error: Throwable ->
        onError(error) ?: handleError(error)
    }

    private fun <T> handleError(error: Throwable) = Observable.error<T>(errorHandler.handle(error))
}