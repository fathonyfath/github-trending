package id.fathonyfath.githubtrending

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class CustomTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, TestGithubApplication::class.java.name, context)
    }
}