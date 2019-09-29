package id.fathonyfath.githubtrending.data.source

import id.fathonyfath.githubtrending.data.source.remote.model.RepositoryJson
import io.reactivex.Observable

interface GithubDataSource {
    fun popularRepositories(): Observable<List<RepositoryJson>>
}