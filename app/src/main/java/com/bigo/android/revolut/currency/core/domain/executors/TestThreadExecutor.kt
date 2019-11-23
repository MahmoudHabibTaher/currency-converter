package com.bigo.android.revolut.currency.core.domain.executors

import dagger.Reusable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

@Reusable
class TestThreadExecutor : ThreadExecutor {
    override fun scheduler(): Scheduler = Schedulers.single()
}