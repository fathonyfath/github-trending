package id.fathonyfath.githubtrending.data

import id.fathonyfath.githubtrending.model.Repository
import io.reactivex.Observable

interface GithubRepository {
    fun repositories(): Observable<List<Repository>>
}