package id.fathonyfath.githubtrending.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import id.fathonyfath.githubtrending.R
import id.fathonyfath.githubtrending.data.GithubRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import javax.inject.Inject

@HiltAndroidTest
class MainActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var githubRepository: GithubRepository

    @BindValue
    lateinit var viewModel: MainViewModel

    private val viewState = MutableLiveData<ViewState>()

    @Before
    fun setUp() {
        hiltRule.inject()
        viewModel = FakeMainViewModel(viewState, githubRepository)
    }

    @Test
    fun loadingContentIsShownWhenViewStateIsLoading() {
        viewState.value = ViewState.Loading

        val scenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.content_loading))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

        onView(withId(R.id.content_no_internet_connection))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))

        onView(withId(R.id.content_repository_list))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))

        scenario.close()
    }

    @Test
    fun noInternetConnectionContentIsShownWhenViewStateIsError() {
        viewState.value = ViewState.Error(IOException())

        val scenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.content_loading))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))

        onView(withId(R.id.content_no_internet_connection))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

        onView(withId(R.id.content_repository_list))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))

        scenario.close()
    }

    @Test
    fun repositoryListContentIsShownWhenViewStateIsSuccess() {
        viewState.value = ViewState.Success(emptyList())

        val scenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.content_loading))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))

        onView(withId(R.id.content_no_internet_connection))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))

        onView(withId(R.id.content_repository_list))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

        scenario.close()
    }

}