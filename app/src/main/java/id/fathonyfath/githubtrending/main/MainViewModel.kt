package id.fathonyfath.githubtrending.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.fathonyfath.githubtrending.data.GithubRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

open class MainViewModel
@Inject constructor(private var githubRepository: GithubRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _state = MutableLiveData<ViewState>()

    private val _repositorySortingAlgorithm = MutableLiveData<SortingType>()

    private val _stateWithSorting = MediatorLiveData<ViewState>()

    open val viewState: LiveData<ViewState>
        get() = _stateWithSorting

    init {
        _state.value = ViewState.Loading

        _repositorySortingAlgorithm.value = SortingType.BY_STARS

        _stateWithSorting.addSource(_state) { handleSorting() }

        _stateWithSorting.addSource(_repositorySortingAlgorithm) { handleSorting() }
    }

    open fun fetchData(forceRefresh: Boolean = false) {
        _state.postValue(ViewState.Loading)

        compositeDisposable.add(
            githubRepository.repositories(clearCache = forceRefresh)
                .subscribeBy(
                    onNext = { _state.postValue(ViewState.Success(it)) },
                    onError = { _state.postValue(ViewState.Error(it)) }
                )
        )
    }

    open fun rearrangeRepositories(sortingType: SortingType) {
        _repositorySortingAlgorithm.postValue(sortingType)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    private fun handleSorting() {
        val currentState = _state.value
        if (currentState is ViewState.Success) {
            val sorted = when (_repositorySortingAlgorithm.value) {
                SortingType.BY_STARS -> currentState.data.sortedBy { it.stars }
                SortingType.BY_NAME -> currentState.data.sortedBy { it.name }
                null -> currentState.data
            }

            _stateWithSorting.postValue(ViewState.Success(sorted))
        } else {
            _stateWithSorting.postValue(_state.value)
        }
    }
}