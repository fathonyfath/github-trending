package id.fathonyfath.githubtrending.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import id.fathonyfath.githubtrending.data.source.remote.RemoteGithubDataSourceTest
import okhttp3.HttpUrl
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, FakeAppModule::class])
interface TestAppComponent : AppComponent {

    fun inject(into: RemoteGithubDataSourceTest)

    @Component.Factory
    interface Factory {
        fun newAppComponent(
            @Named(BASE_URL) @BindsInstance baseUrl: HttpUrl,
            @Named(IS_DEBUG) @BindsInstance isDebug: Boolean
        ): TestAppComponent
    }

    companion object {
        const val BASE_URL = "BaseUrl"
        const val IS_DEBUG = "IsDebug"
    }
}