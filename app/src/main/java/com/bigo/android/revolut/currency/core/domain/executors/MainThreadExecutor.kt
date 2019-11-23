package com.bigo.android.revolut.currency.core.domain.executors

import com.bigo.android.revolut.currency.core.domain.di.MainExecutor
import dagger.Reusable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

@Reusable
@MainExecutor
class MainThreadExecutor : ThreadExecutor {
    override fun scheduler(): Scheduler = AndroidSchedulers.mainThread()
}