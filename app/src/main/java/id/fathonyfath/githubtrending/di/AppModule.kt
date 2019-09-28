package id.fathonyfath.githubtrending.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import id.fathonyfath.githubtrending.data.source.remote.TrendingApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Named
import javax.inject.Singleton

@Module
object AppModule {

    @JvmStatic
    @Provides
    @Singleton
    fun providesOkHttp(@Named(AppComponent.IS_DEBUG) isDebug: Boolean): OkHttpClient {
        return OkHttpClient.Builder().run {
            if (isDebug) {
                this.addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
            return@run this
        }.build()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun providesGson(): Gson {
        return GsonBuilder()
            .create()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun providesRetrofit(
        @Named(AppComponent.BASE_URL) baseUrl: String,
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

    @JvmStatic
    @Provides
    @Singleton
    fun providesTrendingApi(retrofit: Retrofit): TrendingApi {
        return retrofit.create()
    }
}