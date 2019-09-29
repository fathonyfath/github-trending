package id.fathonyfath.githubtrending.data.cache

import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TestTicker
@Inject constructor() : Ticker {

    private var currentTimeInMillis = 0L

    override fun getCurrentTimeMillis(): Long {
        return currentTimeInMillis
    }

    fun advanceTime(duration: Long, timeUnit: TimeUnit) {
        currentTimeInMillis += TimeUnit.MILLISECONDS.convert(duration, timeUnit)
    }

}