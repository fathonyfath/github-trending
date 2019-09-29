package id.fathonyfath.githubtrending.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.fathonyfath.githubtrending.data.GithubRepository
import id.fathonyfath.githubtrending.model.Repository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class MainViewModel
@Inject constructor(private var githubRepository: GithubRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _state = MutableLiveData<State>()

    private val _repositorySortingAlgorithm = MutableLiveData<RepositorySortingAlgorithm>()

    private val _stateWithSorting = MediatorLiveData<State>()

    val state: LiveData<State>
        get() = _stateWithSorting

    init {
        _state.value = State.Loading

        _repositorySortingAlgorithm.value = RepositorySortingAlgorithm.BY_STARS

        _stateWithSorting.addSource(_state) { handleSorting() }

        _stateWithSorting.addSource(_repositorySortingAlgorithm) { handleSorting() }
    }

    fun fetchData(forceRefresh: Boolean = false) {
        _state.postValue(State.Loading)

        compositeDisposable.add(
            githubRepository.repositories(clearCache = forceRefresh)
                .subscribeBy(
                    onNext = { _state.postValue(State.Success(it)) },
                    onError = { _state.postValue(State.Error(it)) }
                )
        )
    }

    fun rearrangeRepositories(sortingAlgorithm: RepositorySortingAlgorithm) {
        _repositorySortingAlgorithm.postValue(sortingAlgorithm)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    private fun handleSorting() {
        val currentState = _state.value
        if (currentState is State.Success) {
            val sorted = when (_repositorySortingAlgorithm.value) {
                RepositorySortingAlgorithm.BY_STARS -> currentState.data.sortedBy { it.stars }
                RepositorySortingAlgorithm.BY_NAME -> currentState.data.sortedBy { it.name }
                null -> currentState.data
            }

            _stateWithSorting.postValue(State.Success(sorted))
        } else {
            _stateWithSorting.postValue(_state.value)
        }
    }

    sealed class State {

        object Loading : State()

        data class Success(val data: List<Repository>) : State()

        class Error(val throwable: Throwable) : State()
    }

    enum class RepositorySortingAlgorithm {
        BY_STARS, BY_NAME
    }
}