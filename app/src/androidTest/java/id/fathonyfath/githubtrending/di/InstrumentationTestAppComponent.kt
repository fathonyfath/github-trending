package id.fathonyfath.githubtrending.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import id.fathonyfath.githubtrending.TestGithubApplication
import id.fathonyfath.githubtrending.data.cache.DefaultRepositoriesCacheTest
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, InstrumentationFakeAppModule::class])
interface InstrumentationTestAppComponent : AppComponent {

    fun inject(into: TestGithubApplication)
    fun inject(into: DefaultRepositoriesCacheTest)

    @Component.Factory
    interface Factory {
        fun newAppComponent(
            @BindsInstance context: Context
        ): InstrumentationTestAppComponent
    }
}