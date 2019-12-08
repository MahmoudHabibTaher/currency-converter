package com.bigo.android.sample.currency.core.domain.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BackgroundExecutor

@Qualifier
@Retention
annotation class MainExecutor

