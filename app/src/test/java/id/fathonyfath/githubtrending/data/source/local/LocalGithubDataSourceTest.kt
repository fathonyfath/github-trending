package id.fathonyfath.githubtrending.data.source.local

import id.fathonyfath.githubtrending.data.cache.RepositoriesCache
import id.fathonyfath.githubtrending.scheduler.TrampolineSchedulerProvider
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LocalGithubDataSourceTest {

    @Mock
    lateinit var repositoriesCache: RepositoriesCache

    lateinit var localGithubDataSource: LocalGithubDataSource

    @Before
    fun setUp() {
        localGithubDataSource =
            LocalGithubDataSource(repositoriesCache, TrampolineSchedulerProvider())
    }

    @Test
    fun popularRepositories() {
        `when`(repositoriesCache.get()).thenReturn(Observable.just(emptyList()))

        localGithubDataSource.popularRepositories()

        verify(repositoriesCache, times(1)).get()
    }
}