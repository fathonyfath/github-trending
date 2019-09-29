package id.fathonyfath.githubtrending.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object InstrumentationFakeAppModule {

    @JvmStatic
    @Provides
    @Singleton
    fun providesSharedPreference(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @JvmStatic
    @Provides
    @Singleton
    fun providesGson(): Gson {
        return GsonBuilder()
            .create()
    }
}