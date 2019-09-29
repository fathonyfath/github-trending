package id.fathonyfath.githubtrending.data.cache

class DefaultTicker : Ticker {

    override fun getCurrentTimeMillis(): Long {
        return System.currentTimeMillis()
    }

}