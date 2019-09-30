package id.fathonyfath.githubtrending.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import id.fathonyfath.githubtrending.data.GithubRepository
import id.fathonyfath.githubtrending.model.Repository
import io.reactivex.Observable
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.io.IOException

@RunWith(JUnit4::class)
class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var githubRepository: GithubRepository

    @Mock
    lateinit var viewStateObserver: Observer<ViewState>

    lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @After
    fun tearDown() {

    }

    @Test
    fun init_stateShouldBeLoading() {
        mainViewModel = MainViewModel(githubRepository)
        mainViewModel.viewState.observeForever(viewStateObserver)

        verify(viewStateObserver, atLeastOnce()).onChanged(ViewState.Loading)
    }

    @Test
    fun fetchData_withSuccessResult() {
        `when`(githubRepository.repositories(ArgumentMatchers.anyBoolean())).thenReturn(
            Observable.just(emptyList())
        )

        mainViewModel = MainViewModel(githubRepository)
        mainViewModel.viewState.observeForever(viewStateObserver)
        mainViewModel.fetchData(false)

        verify(viewStateObserver, atLeastOnce()).onChanged(ViewState.Success(emptyList()))
    }

    @Test
    fun fetchData_withErrorResult() {
        `when`(githubRepository.repositories(ArgumentMatchers.anyBoolean())).thenReturn(
            Observable.error(IOException())
        )

        mainViewModel = MainViewModel(githubRepository)
        mainViewModel.viewState.observeForever(viewStateObserver)
        mainViewModel.fetchData(false)

        val argumentCaptor = ArgumentCaptor.forClass(ViewState.Error::class.java)
        verify(viewStateObserver, atLeastOnce()).onChanged(argumentCaptor.capture())

        assertTrue(argumentCaptor.value.throwable is IOException)
    }

    @Test
    fun fetchData_sortByStars() {
        val dummyData = listOf(
            Repository(
                "author-1",
                "name-1",
                "avatar-1",
                "url-1",
                "desc-1",
                "lang-1",
                "color-1",
                9,
                0,
                0,
                emptyList()
            ),
            Repository(
                "author-2",
                "name-2",
                "avatar-2",
                "url-2",
                "desc-2",
                "lang-2",
                "color-2",
                1,
                0,
                0,
                emptyList()
            ),
            Repository(
                "author-3",
                "name-3",
                "avatar-3",
                "url-3",
                "desc-3",
                "lang-3",
                "color-3",
                3,
                0,
                0,
                emptyList()
            )
        )

        val sortedByStars = dummyData.sortedBy { it.stars }

        `when`(githubRepository.repositories(ArgumentMatchers.anyBoolean())).thenReturn(
            Observable.just(dummyData)
        )

        mainViewModel = MainViewModel(githubRepository)
        mainViewModel.viewState.observeForever(viewStateObserver)
        mainViewModel.fetchData(false)

        mainViewModel.rearrangeRepositories(SortingType.BY_STARS)

        verify(viewStateObserver, atLeastOnce()).onChanged(
            ViewState.Success(sortedByStars)
        )
    }

    @Test
    fun fetchData_sortByName() {
        val dummyData = listOf(
            Repository(
                "author-1",
                "lorem",
                "avatar-1",
                "url-1",
                "desc-1",
                "lang-1",
                "color-1",
                0,
                0,
                0,
                emptyList()
            ),
            Repository(
                "author-2",
                "buzz",
                "avatar-2",
                "url-2",
                "desc-2",
                "lang-2",
                "color-2",
                0,
                0,
                0,
                emptyList()
            ),
            Repository(
                "author-3",
                "fizz",
                "avatar-3",
                "url-3",
                "desc-3",
                "lang-3",
                "color-3",
                0,
                0,
                0,
                emptyList()
            )
        )

        val sortedByName = dummyData.sortedBy { it.name }

        `when`(githubRepository.repositories(ArgumentMatchers.anyBoolean())).thenReturn(
            Observable.just(dummyData)
        )

        mainViewModel = MainViewModel(githubRepository)
        mainViewModel.viewState.observeForever(viewStateObserver)
        mainViewModel.fetchData(false)

        mainViewModel.rearrangeRepositories(SortingType.BY_NAME)

        verify(viewStateObserver, atLeastOnce()).onChanged(
            ViewState.Success(sortedByName)
        )
    }
}