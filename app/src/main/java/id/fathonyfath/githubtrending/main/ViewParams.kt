package id.fathonyfath.githubtrending.main

import id.fathonyfath.githubtrending.model.Repository

sealed class ViewState {

    object Loading : ViewState()

    data class Success(val data: List<Repository>) : ViewState()

    class Error(val throwable: Throwable) : ViewState()
}

enum class SortingType {
    BY_STARS, BY_NAME
}