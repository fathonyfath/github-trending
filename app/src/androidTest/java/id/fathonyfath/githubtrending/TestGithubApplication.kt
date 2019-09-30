package id.fathonyfath.githubtrending

import android.app.Activity
import android.app.Application
import androidx.test.platform.app.InstrumentationRegistry
import dagger.android.AndroidInjector
import dagger.android.HasAndroidInjector
import id.fathonyfath.githubtrending.di.DaggerInstrumentationTestAppComponent
import id.fathonyfath.githubtrending.di.InstrumentationTestAppComponent

class TestGithubApplication : Application(), HasAndroidInjector {

    private lateinit var _component: InstrumentationTestAppComponent
    val component: InstrumentationTestAppComponent
        get() = _component

    override fun onCreate() {
        super.onCreate()

        _component = DaggerInstrumentationTestAppComponent.factory().newAppComponent(this)

        _component.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return fakeAndroidInjector("injected by application")
    }

    companion object {

        val instance: TestGithubApplication
            get() {
                return InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
                        as TestGithubApplication
            }

        fun fakeAndroidInjector(tag: String) = AndroidInjector<Any> { anyObject ->
            if (anyObject is InjectableActivity) anyObject.tag = tag
        }
    }

    private class InjectableActivity : Activity() {
        var tag: String = ""
    }
}