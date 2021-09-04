package id.fathonyfath.githubtrending.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import id.fathonyfath.githubtrending.data.source.remote.TrendingApi
import id.fathonyfath.githubtrending.scheduler.SchedulerProvider
import id.fathonyfath.githubtrending.scheduler.TrampolineSchedulerProvider
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
object FakeAppModule {

    @Provides
    @Singleton
    fun providesOkHttp(@IsDebug isDebug: Boolean): OkHttpClient {
        return OkHttpClient.Builder().run {
            if (isDebug) {
                this.addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
            return@run this
        }
            .readTimeout(100, TimeUnit.MILLISECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun providesGson(): Gson {
        return GsonBuilder()
            .create()
    }

    @Provides
    @Singleton
    fun providesRetrofit(
        @BaseUrl baseUrl: HttpUrl,
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesTrendingApi(retrofit: Retrofit): TrendingApi {
        return retrofit.create()
    }

    @Provides
    @Singleton
    fun providesSchedulerProvider(trampolineSchedulerProvider: TrampolineSchedulerProvider): SchedulerProvider {
        return trampolineSchedulerProvider
    }
}