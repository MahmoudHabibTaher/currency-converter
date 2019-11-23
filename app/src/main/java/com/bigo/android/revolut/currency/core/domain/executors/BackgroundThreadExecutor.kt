package com.bigo.android.revolut.currency.core.domain.executors

import com.bigo.android.revolut.currency.core.domain.di.BackgroundExecutor
import dagger.Reusable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

@Reusable
@BackgroundExecutor
class BackgroundThreadExecutor : ThreadExecutor {
    override fun scheduler(): Scheduler = Schedulers.io()
}