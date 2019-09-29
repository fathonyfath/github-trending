package id.fathonyfath.githubtrending

import android.app.Application
import androidx.test.platform.app.InstrumentationRegistry
import id.fathonyfath.githubtrending.di.DaggerInstrumentationTestAppComponent
import id.fathonyfath.githubtrending.di.InstrumentationTestAppComponent

class TestGithubApplication : Application() {

    private lateinit var _component: InstrumentationTestAppComponent
    val component: InstrumentationTestAppComponent
        get() = _component

    override fun onCreate() {
        super.onCreate()

        _component = DaggerInstrumentationTestAppComponent.factory().newAppComponent(this)
    }

    companion object {

        val instance: TestGithubApplication
            get() {
                return InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
                        as TestGithubApplication
            }

    }
}