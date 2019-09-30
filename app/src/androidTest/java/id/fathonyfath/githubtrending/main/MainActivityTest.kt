package id.fathonyfath.githubtrending.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import id.fathonyfath.githubtrending.R
import id.fathonyfath.githubtrending.di.TestViewModelFactory
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, false, false)


    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var viewModel: MainViewModel

    private val viewState = MutableLiveData<ViewState>()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        `when`(viewModel.viewState).thenReturn(viewState)

        activityRule.activity.mainViewModelFactory = TestViewModelFactory(viewModel)

        activityRule.launchActivity(null)
    }

    @After
    fun tearDown() {
        activityRule.finishActivity()
    }

    @Test
    fun loadingContentIsShownWhenViewStateIsLoading() {
        viewState.postValue(ViewState.Loading)

        onView(withId(R.id.content_loading))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

        onView(withId(R.id.content_no_internet_connection))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))

        onView(withId(R.id.content_repository_list))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

}