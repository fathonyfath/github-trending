package id.fathonyfath.githubtrending.data.source.remote

import id.fathonyfath.githubtrending.BuildConfig
import id.fathonyfath.githubtrending.di.DaggerTestAppComponent
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import javax.inject.Inject

class RemoteGithubDataSourceTest {

    @Inject
    lateinit var remoteGithubDataSource: RemoteGithubDataSource

    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start()

        DaggerTestAppComponent
            .factory()
            .newAppComponent(mockWebServer.url("/"), BuildConfig.DEBUG)
            .inject(this)

    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun popularRepositories_positiveResponse() {
        val responseBody = requireNotNull(readFileFromResources("./responses/repositories.json"))
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(responseBody)

        mockWebServer.enqueue(response)

        remoteGithubDataSource.popularRepositories()
            .test()
            .assertNoErrors()
            .dispose()
    }

    @Test
    fun popularRepositories_negativeResponse() {
        val response = MockResponse()
            .setSocketPolicy(SocketPolicy.NO_RESPONSE)

        mockWebServer.enqueue(response)

        remoteGithubDataSource.popularRepositories()
            .test()
            .assertError(SocketTimeoutException::class.java)
    }

    private fun readFileFromResources(path: String): String? {
        return javaClass.classLoader?.getResource(path)?.readText()
    }
}