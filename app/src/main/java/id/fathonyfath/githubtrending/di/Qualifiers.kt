package id.fathonyfath.githubtrending.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class BaseUrl

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IsDebug

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LocalDataSource

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RemoteDataSource