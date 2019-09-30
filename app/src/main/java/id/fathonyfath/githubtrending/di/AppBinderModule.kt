package id.fathonyfath.githubtrending.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import id.fathonyfath.githubtrending.data.DefaultGithubRepository
import id.fathonyfath.githubtrending.data.GithubRepository
import id.fathonyfath.githubtrending.data.cache.DefaultRepositoriesCache
import id.fathonyfath.githubtrending.data.cache.DefaultTicker
import id.fathonyfath.githubtrending.data.cache.RepositoriesCache
import id.fathonyfath.githubtrending.data.cache.Ticker
import id.fathonyfath.githubtrending.data.source.GithubDataSource
import id.fathonyfath.githubtrending.data.source.local.LocalGithubDataSource
import id.fathonyfath.githubtrending.data.source.remote.RemoteGithubDataSource
import id.fathonyfath.githubtrending.data.source.remote.TrendingApi
import id.fathonyfath.githubtrending.scheduler.DefaultSchedulerProvider
import id.fathonyfath.githubtrending.scheduler.SchedulerProvider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Named
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
    @Named(AppComponent.LOCAL_DATA_SOURCE)
    abstract fun providesLocalGithubDataSource(localGithubDataSource: LocalGithubDataSource): GithubDataSource

    @Binds
    @Singleton
    @Named(AppComponent.REMOTE_DATA_SOURCE)
    abstract fun providesRemoteGithubDataSource(remoteGithubDataSource: RemoteGithubDataSource): GithubDataSource

    @Binds
    @Singleton
    abstract fun providesGithubRepository(githubRepository: DefaultGithubRepository): GithubRepository
}