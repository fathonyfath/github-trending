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
            @BaseUrl @BindsInstance baseUrl: HttpUrl,
            @IsDebug @BindsInstance isDebug: Boolean
        ): TestAppComponent
    }
}