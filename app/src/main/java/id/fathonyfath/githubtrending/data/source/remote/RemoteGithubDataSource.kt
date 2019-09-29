package id.fathonyfath.githubtrending.data.source.remote

import id.fathonyfath.githubtrending.data.source.GithubDataSource
import id.fathonyfath.githubtrending.model.Repository
import id.fathonyfath.githubtrending.scheduler.SchedulerProvider
import io.reactivex.Observable
import javax.inject.Inject

class RemoteGithubDataSource
@Inject constructor(
    private val trendingApi: TrendingApi,
    private val schedulerProvider: SchedulerProvider
) : GithubDataSource {

    override fun popularRepositories(): Observable<List<Repository>> {
        return trendingApi.getRepositories()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.main())
    }

}