package id.fathonyfath.githubtrending

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import id.fathonyfath.githubtrending.di.DaggerAppComponent
import javax.inject.Inject

class GithubApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

        initializeDagger()
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }


    private fun initializeDagger() {
        DaggerAppComponent.factory().newAppComponent(
            this,
            BuildConfig.BASE_URL,
            BuildConfig.DEBUG
        ).inject(this)
    }
}