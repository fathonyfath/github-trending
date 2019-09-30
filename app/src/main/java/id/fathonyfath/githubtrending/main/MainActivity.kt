package id.fathonyfath.githubtrending.main

import android.os.Bundle
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
}
