package id.fathonyfath.githubtrending.data.cache

import android.content.Context
import android.content.SharedPreferences
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import id.fathonyfath.githubtrending.TestGithubApplication
import id.fathonyfath.githubtrending.model.Contributor
import id.fathonyfath.githubtrending.model.Repository
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class DefaultRepositoriesCacheTest {

    @Inject
    lateinit var appContext: Context

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var ticker: TestTicker

    lateinit var defaultRepositoriesCache: DefaultRepositoriesCache

    private val dummyData = listOf(
        Repository(
            "Foo",
            "foo-bar",
            "localhost",
            "localhost",
            "Foo is bar",
            "HTML",
            "#5dad3d",
            751,
            8,
            0,
            listOf(
                Contributor("Foo", "localhost", "localhost"),
                Contributor("Bar", "localhost", "localhost")
            )
        ),
        Repository(
            "Bar",
            "bar-foo",
            "localhost",
            "localhost",
            "Bar is foo",
            "CSS",
            "#481952",
            111,
            6,
            90,
            listOf(
                Contributor("Baz", "localhost", "localhost")
            )
        )
    )

    @Before
    fun setUp() {
        TestGithubApplication.instance.component.inject(this)

        defaultRepositoriesCache =
            DefaultRepositoriesCache(appContext, sharedPreferences, gson, ticker)
    }

    @After
    fun tearDown() {
        defaultRepositoriesCache.evictAll().blockingGet()
    }

    @Test
    fun get_whileExist() {
        defaultRepositoriesCache.put(dummyData)
            .test()
            .assertNoErrors()
            .dispose()

        defaultRepositoriesCache.get()
            .test()
            .assertValue { it == dummyData }
            .dispose()

        defaultRepositoriesCache.evictAll().blockingGet()
    }

    @Test
    fun get_whileEmpty() {
        defaultRepositoriesCache.evictAll().blockingGet()

        defaultRepositoriesCache.get()
            .test()
            .assertError(CacheNotFoundException::class.java)
            .dispose()
    }

    @Test
    fun get_whileExistThenExpired() {
        defaultRepositoriesCache.put(dummyData)
            .test()
            .assertNoErrors()
            .dispose()

        defaultRepositoriesCache.get()
            .test()
            .assertValue { it == dummyData }
            .dispose()

        ticker.advanceTime(3, TimeUnit.HOURS)

        defaultRepositoriesCache.get()
            .test()
            .assertError(CacheNotFoundException::class.java)
            .dispose()

        defaultRepositoriesCache.evictAll().blockingGet()
    }

    @Test
    fun isCached_whileEmpty() {
        defaultRepositoriesCache.evictAll().blockingGet()

        defaultRepositoriesCache.isCached()
            .test()
            .assertValue { !it }
    }

    @Test
    fun isCached_whileExist() {
        defaultRepositoriesCache.evictAll().blockingGet()

        defaultRepositoriesCache.put(dummyData)
            .test()
            .assertNoErrors()
            .dispose()

        defaultRepositoriesCache.isCached()
            .test()
            .assertValue { it }
    }
}