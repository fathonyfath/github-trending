package id.fathonyfath.githubtrending.data

import id.fathonyfath.githubtrending.data.cache.CacheNotFoundException
import id.fathonyfath.githubtrending.data.source.GithubDataSource
import id.fathonyfath.githubtrending.scheduler.TrampolineSchedulerProvider
import io.reactivex.Observable
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DefaultGithubRepositoryTest {

    @Mock
    lateinit var remoteGithubDataSource: GithubDataSource

    @Mock
    lateinit var localGithubDataSource: GithubDataSource

    lateinit var schedulerProvider: TrampolineSchedulerProvider

    lateinit var githubRepository: GithubRepository

    @Before
    fun setUp() {
        schedulerProvider = TrampolineSchedulerProvider()

        githubRepository = DefaultGithubRepository(
            localGithubDataSource,
            remoteGithubDataSource,
            schedulerProvider
        )
    }

    @After
    fun tearDown() {

    }

    @Test
    fun repositories_whenLocalThrowErrorExecuteFromRemote() {
        `when`(localGithubDataSource.popularRepositories()).thenReturn(
            Observable.error(CacheNotFoundException())
        )

        `when`(remoteGithubDataSource.popularRepositories()).thenReturn(Observable.just(emptyList()))

        githubRepository.repositories()
            .test()
            .assertValue { it.isEmpty() }
            .dispose()

        verify(localGithubDataSource, atLeastOnce()).popularRepositories()
        verify(remoteGithubDataSource, times(1)).popularRepositories()
    }

    @Test
    fun repositories_whenLocalExistSkipFromRemote() {
        `when`(localGithubDataSource.popularRepositories()).thenReturn(Observable.just(emptyList()))

        githubRepository.repositories()
            .test()
            .assertValue { it.isEmpty() }
            .dispose()

        verify(localGithubDataSource, times(1)).popularRepositories()
        verify(remoteGithubDataSource, never()).popularRepositories()
    }
}