package id.fathonyfath.githubtrending.data.cache

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import id.fathonyfath.githubtrending.model.Contributor
import id.fathonyfath.githubtrending.model.Repository
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidTest
class DefaultRepositoriesCacheTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    @ApplicationContext
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
            1,
            "Foo",
            "foo-bar",
            "localhost",
            "Foo is bar",
            "HTML",
            "#5dad3d",
            751,
            8,
            0,
            "since",
            listOf(
                Contributor("Foo", "localhost", "localhost"),
                Contributor("Bar", "localhost", "localhost")
            )
        ),
        Repository(
            2,
            "Bar",
            "bar-foo",
            "localhost",
            "Bar is foo",
            "CSS",
            "#481952",
            111,
            6,
            90,
            "since",
            listOf(
                Contributor("Baz", "localhost", "localhost")
            )
        )
    )

    @Before
    fun setUp() {
        hiltRule.inject()

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