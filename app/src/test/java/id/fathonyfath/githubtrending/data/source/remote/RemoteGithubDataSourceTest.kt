package id.fathonyfath.githubtrending.data.source.remote

import id.fathonyfath.githubtrending.BuildConfig
import id.fathonyfath.githubtrending.data.cache.RepositoriesCache
import id.fathonyfath.githubtrending.scheduler.TrampolineSchedulerProvider
import io.reactivex.Completable
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import javax.inject.Inject

@RunWith(MockitoJUnitRunner::class)
class RemoteGithubDataSourceTest {

    @Inject
    lateinit var trendingApi: TrendingApi

    @Mock
    lateinit var repositoriesCache: RepositoriesCache

    @Inject
    lateinit var schedulerProvider: TrampolineSchedulerProvider

    lateinit var remoteGithubDataSource: RemoteGithubDataSource

    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start()

        DaggerTestAppComponent
            .factory()
            .newAppComponent(mockWebServer.url("/"), BuildConfig.DEBUG)
            .inject(this)

        remoteGithubDataSource =
            RemoteGithubDataSource(trendingApi, repositoriesCache, schedulerProvider)

    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun popularRepositories_positiveResponse() {
        `when`(repositoriesCache.put(ArgumentMatchers.anyList())).thenReturn(Completable.complete())

        val responseBody = requireNotNull(readFileFromResources("./responses/repositories.json"))
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(responseBody)

        mockWebServer.enqueue(response)

        remoteGithubDataSource.popularRepositories()
            .test()
            .assertNoErrors()
            .dispose()

        verify(repositoriesCache, times(1)).put(ArgumentMatchers.anyList())
    }

    @Test
    fun popularRepositories_negativeResponse() {
        val response = MockResponse()
            .setSocketPolicy(SocketPolicy.NO_RESPONSE)

        mockWebServer.enqueue(response)

        remoteGithubDataSource.popularRepositories()
            .test()
            .assertError(SocketTimeoutException::class.java)
            .dispose()
    }

    private fun readFileFromResources(path: String): String? {
        return javaClass.classLoader?.getResource(path)?.readText()
    }
}