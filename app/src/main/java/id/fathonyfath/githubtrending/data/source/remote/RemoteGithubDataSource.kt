package id.fathonyfath.githubtrending.data.source.remote

import id.fathonyfath.githubtrending.core.RepositoryResponseToRepositoryMapper
import id.fathonyfath.githubtrending.core.mapUsing
import id.fathonyfath.githubtrending.data.cache.RepositoriesCache
import id.fathonyfath.githubtrending.data.source.GithubDataSource
import id.fathonyfath.githubtrending.model.Repository
import id.fathonyfath.githubtrending.scheduler.SchedulerProvider
import io.reactivex.Observable
import javax.inject.Inject

class RemoteGithubDataSource
@Inject constructor(
    private val trendingApi: TrendingApi,
    private val repositoriesCache: RepositoriesCache,
    private val schedulerProvider: SchedulerProvider,
    private val mapper: RepositoryResponseToRepositoryMapper
) : GithubDataSource {

    override fun popularRepositories(): Observable<List<Repository>> {
        return trendingApi.getRepositories()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.main())
            .map { it.mapUsing(mapper) }
            .flatMap { repositories ->
                return@flatMap repositoriesCache.put(repositories)
                    .andThen(Observable.just(repositories))
            }
    }

}