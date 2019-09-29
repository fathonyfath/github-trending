package id.fathonyfath.githubtrending.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import id.fathonyfath.githubtrending.GithubApplication
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class, ActivityBindingModule::class])
interface AppComponent {

    fun inject(app: GithubApplication)

    @Component.Factory
    interface Factory {
        fun newAppComponent(
            @BindsInstance context: Context,
            @Named(BASE_URL) @BindsInstance baseUrl: String,
            @Named(IS_DEBUG) @BindsInstance isDebug: Boolean
        ): AppComponent
    }

    companion object {
        const val BASE_URL = "BaseUrl"
        const val IS_DEBUG = "IsDebug"
        const val LOCAL_DATA_SOURCE = "LocalDataSource"
        const val REMOTE_DATA_SOURCE = "RemoteDataSource"
    }
}