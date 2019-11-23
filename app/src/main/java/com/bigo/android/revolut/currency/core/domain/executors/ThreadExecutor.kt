package com.bigo.android.revolut.currency.core.domain.executors

import io.reactivex.Scheduler

interface ThreadExecutor {
    fun scheduler(): Scheduler
}