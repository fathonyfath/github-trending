package id.fathonyfath.githubtrending.di

import dagger.Binds
import dagger.Module
import id.fathonyfath.githubtrending.data.DefaultGithubRepository
import id.fathonyfath.githubtrending.data.GithubRepository
import id.fathonyfath.githubtrending.data.cache.DefaultRepositoriesCache
import id.fathonyfath.githubtrending.data.cache.DefaultTicker
import id.fathonyfath.githubtrending.data.cache.RepositoriesCache
import id.fathonyfath.githubtrending.data.cache.Ticker
import id.fathonyfath.githubtrending.data.source.GithubDataSource
import id.fathonyfath.githubtrending.data.source.local.LocalGithubDataSource
import id.fathonyfath.githubtrending.data.source.remote.RemoteGithubDataSource
import id.fathonyfath.githubtrending.scheduler.DefaultSchedulerProvider
import id.fathonyfath.githubtrending.scheduler.SchedulerProvider
import javax.inject.Singleton

@Module
abstract class AppBinderModule {

    @Binds
    @Singleton
    abstract fun providesTicker(ticker: DefaultTicker): Ticker

    @Binds
    @Singleton
    abstract fun providesRepositoriesCache(repositoriesCache: DefaultRepositoriesCache): RepositoriesCache

    @Binds
    @Singleton
    abstract fun providesSchedulerProvider(schedulerProvider: DefaultSchedulerProvider): SchedulerProvider

    @Binds
    @Singleton
    @LocalDataSource
    abstract fun providesLocalGithubDataSource(localGithubDataSource: LocalGithubDataSource): GithubDataSource

    @Binds
    @Singleton
    @RemoteDataSource
    abstract fun providesRemoteGithubDataSource(remoteGithubDataSource: RemoteGithubDataSource): GithubDataSource

    @Binds
    @Singleton
    abstract fun providesGithubRepository(githubRepository: DefaultGithubRepository): GithubRepository
}