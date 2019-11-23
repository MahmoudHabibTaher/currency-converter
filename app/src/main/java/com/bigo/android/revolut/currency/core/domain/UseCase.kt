package com.bigo.android.revolut.currency.core.domain

import com.bigo.android.revolut.currency.core.domain.executors.ThreadExecutor
import io.reactivex.Observable

abstract class UseCase<T, P : Params>(
    private val backgroundExecutor: ThreadExecutor,
    private val mainExecutor: ThreadExecutor
) {

    protected abstract fun buildUseCase(params: P?): Observable<T>

    fun getObservable(params: P? = null) =
        buildUseCase(params)
            .subscribeOn(backgroundExecutor.scheduler())
            .observeOn(mainExecutor.scheduler())
}