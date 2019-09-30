package id.fathonyfath.githubtrending.main

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dagger.android.AndroidInjection
import id.fathonyfath.githubtrending.R
import id.fathonyfath.githubtrending.di.ViewModelFactory
import id.fathonyfath.githubtrending.model.Repository
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    private lateinit var contentLoading: FrameLayout
    private lateinit var contentNoInternetConnection: ConstraintLayout
    private lateinit var contentRepositoryList: RecyclerView

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var retryButton: Button

    private lateinit var repositoryAdapter: RepositoryAdapter

    private var layoutManagerState: Parcelable? = null
    private var adapterState: Parcelable? = null

    @Inject
    lateinit var mainViewModelFactory: ViewModelFactory<MainViewModel>

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        if (::mainViewModelFactory.isInitialized)
            viewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)

        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        contentLoading = findViewById(R.id.content_loading)
        contentNoInternetConnection = findViewById(R.id.content_no_internet_connection)
        contentRepositoryList = findViewById(R.id.content_repository_list)
        swipeRefreshLayout = findViewById(R.id.swipe_refresh)
        retryButton = findViewById(R.id.retry_button)

        setupRecyclerView()
        setupDefaultState()

        toolbar.inflateMenu(R.menu.main_menu)

        observeViewState()
    }

    override fun onResume() {
        super.onResume()

        layoutManagerState?.let { contentRepositoryList.layoutManager?.onRestoreInstanceState(it) }
        adapterState?.let { repositoryAdapter.onRestoreInstanceState(it) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        contentRepositoryList.layoutManager?.let { recyclerLayoutManager ->
            layoutManagerState = recyclerLayoutManager.onSaveInstanceState()
            outState.putParcelable(LAYOUT_MANAGER_STATE_KEY, layoutManagerState)
        }

        adapterState = repositoryAdapter.onSaveInstanceState()
        outState.putParcelable(ADAPTER_STATE_KEY, adapterState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        savedInstanceState?.let { savedState ->
            layoutManagerState = savedState.getParcelable(LAYOUT_MANAGER_STATE_KEY)
            adapterState = savedState.getParcelable(ADAPTER_STATE_KEY)
        }
    }

    @VisibleForTesting
    fun injectViewModelProvider(factory: ViewModelFactory<MainViewModel>) {
        mainViewModelFactory = factory
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
        observeViewState()
    }

    private fun setupRecyclerView() {
        repositoryAdapter = RepositoryAdapter(this)

        contentRepositoryList.layoutManager = LinearLayoutManager(this)
        contentRepositoryList.adapter = repositoryAdapter
    }

    private fun setupDefaultState() {
        contentLoading.visibility = View.GONE
        contentNoInternetConnection.visibility = View.GONE
        contentRepositoryList.visibility = View.GONE
    }

    private fun updateRecyclerList(repositories: List<Repository>) {
        repositoryAdapter.submitList(repositories)
    }

    private fun observeViewState() {
        if (::viewModel.isInitialized) {
            viewModel.fetchData()

            viewModel.viewState.observe(this) { viewState ->
                when (viewState) {
                    is ViewState.Loading -> {
                        contentLoading.visibility = View.VISIBLE
                        contentRepositoryList.visibility = View.GONE
                        contentNoInternetConnection.visibility = View.GONE
                    }
                    is ViewState.Error -> {
                        contentLoading.visibility = View.GONE
                        contentRepositoryList.visibility = View.GONE
                        contentNoInternetConnection.visibility = View.VISIBLE
                    }
                    is ViewState.Success -> {
                        contentLoading.visibility = View.GONE
                        contentRepositoryList.visibility = View.VISIBLE
                        contentNoInternetConnection.visibility = View.GONE

                        updateRecyclerList(viewState.data)
                    }
                }
            }
        }
    }

    companion object {
        private const val LAYOUT_MANAGER_STATE_KEY = "LayoutManagerState"
        private const val ADAPTER_STATE_KEY = "AdapterState"
    }
}
