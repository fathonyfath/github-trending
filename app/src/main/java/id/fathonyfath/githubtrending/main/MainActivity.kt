package id.fathonyfath.githubtrending.main

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.fathonyfath.githubtrending.R
import id.fathonyfath.githubtrending.model.Contributor
import id.fathonyfath.githubtrending.model.Repository

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    private lateinit var contentLoading: FrameLayout
    private lateinit var contentNoInternetConnection: ConstraintLayout
    private lateinit var contentRepositoryList: RecyclerView

    private lateinit var repositoryAdapter: RepositoryAdapter

    private var layoutManagerState: Parcelable? = null
    private var adapterState: Parcelable? = null

    private val dummyData = listOf(
        Repository(
            "Foo",
            "foo-bar",
            "https://github.com/tmrowco.png",
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
            "https://github.com/ondyari.png",
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        contentLoading = findViewById(R.id.content_loading)
        contentNoInternetConnection = findViewById(R.id.content_no_internet_connection)
        contentRepositoryList = findViewById(R.id.content_repository_list)

        repositoryAdapter = RepositoryAdapter(this)

        contentRepositoryList.layoutManager = LinearLayoutManager(this)
        contentRepositoryList.adapter = repositoryAdapter

        repositoryAdapter.submitList(dummyData)

        contentLoading.visibility = View.GONE
        contentNoInternetConnection.visibility = View.GONE

        toolbar.inflateMenu(R.menu.main_menu)
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

    companion object {
        private const val LAYOUT_MANAGER_STATE_KEY = "LayoutManagerState"
        private const val ADAPTER_STATE_KEY = "AdapterState"
    }
}
