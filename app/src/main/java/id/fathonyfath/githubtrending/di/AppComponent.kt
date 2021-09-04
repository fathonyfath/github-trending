package id.fathonyfath.githubtrending.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import id.fathonyfath.githubtrending.GithubApplication
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidInjectionModule::class,
        AppBinderModule::class,
        AppProviderModule::class,
        ActivityBindingModule::class]
)
interface AppComponent {

    fun inject(app: GithubApplication)

    @Component.Factory
    interface Factory {
        fun newAppComponent(
            @BindsInstance context: Context,
            @BaseUrl @BindsInstance baseUrl: String,
            @IsDebug @BindsInstance isDebug: Boolean
        ): AppComponent
    }
}