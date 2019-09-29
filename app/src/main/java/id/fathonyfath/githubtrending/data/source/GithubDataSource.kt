package id.fathonyfath.githubtrending.data.source

import id.fathonyfath.githubtrending.model.Repository
import io.reactivex.Observable

interface GithubDataSource {
    fun popularRepositories(): Observable<List<Repository>>
}