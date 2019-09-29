package id.fathonyfath.githubtrending.data.cache

import javax.inject.Inject

class DefaultTicker
@Inject constructor() : Ticker {

    override fun getCurrentTimeMillis(): Long {
        return System.currentTimeMillis()
    }

}