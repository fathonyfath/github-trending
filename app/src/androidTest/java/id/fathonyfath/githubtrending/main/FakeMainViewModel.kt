package id.fathonyfath.githubtrending.main

import androidx.lifecycle.LiveData
import id.fathonyfath.githubtrending.data.GithubRepository

class FakeMainViewModel
constructor(
    override val viewState: LiveData<ViewState>,
    githubRepository: GithubRepository
) : MainViewModel(githubRepository)