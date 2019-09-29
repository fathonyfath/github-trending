package id.fathonyfath.githubtrending.data.cache

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import id.fathonyfath.githubtrending.model.Repository
import id.fathonyfath.githubtrending.utils.fromJson
import io.reactivex.Completable
import io.reactivex.Observable
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultRepositoriesCache
@Inject constructor(
    context: Context,
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
    private val ticker: Ticker
) : RepositoriesCache {

    private val cacheDir: File = context.cacheDir

    override fun get(): Observable<List<Repository>> {
        return isExpired()
            .map { isExpired ->
                if (!isExpired && cacheFile(cacheDir).exists()) {
                    val content = cacheFile(cacheDir).readText()
                    return@map gson.fromJson<List<Repository>>(content)
                        ?: throw CacheNotFoundException()
                } else {
                    throw CacheNotFoundException()
                }
            }
    }

    override fun put(repositories: List<Repository>): Completable {
        return Completable.create { emitter ->
            val json = gson.toJson(repositories)
            cacheFile(cacheDir).writeText(json)
            setLastCacheUpdateInMillis()
            emitter.onComplete()
        }
    }

    override fun isCached(): Observable<Boolean> {
        return Observable.create { emitter ->
            emitter.onNext(cacheFile(cacheDir).exists())
            emitter.onComplete()
        }
    }

    override fun isExpired(): Observable<Boolean> {
        return Observable.create<Boolean> { emitter ->
            val currentTime = ticker.getCurrentTimeMillis()
            val lastUpdateTime = getLastCacheUpdateInMillis()

            val isExpired = ((currentTime - lastUpdateTime) > EXPIRATION_TIME)
            emitter.onNext(isExpired)
            emitter.onComplete()
        }
            .flatMap<Boolean> { isExpired ->
                if (isExpired) {
                    return@flatMap evictAll().andThen(Observable.just(isExpired))
                } else {
                    return@flatMap Observable.just(isExpired)
                }
            }
    }

    override fun evictAll(): Completable {
        return Completable.create { emitter ->
            cacheFile(cacheDir).delete()
            emitter.onComplete()
        }
    }

    private fun cacheFile(cacheDir: File): File {
        return File(cacheDir, FILE_NAME)
    }

    private fun setLastCacheUpdateInMillis() {
        val tickerTime = ticker.getCurrentTimeMillis()
        sharedPreferences.edit {
            putLong(LAST_UPDATE_KEY, tickerTime)
        }
    }

    private fun getLastCacheUpdateInMillis(): Long {
        return sharedPreferences.getLong(LAST_UPDATE_KEY, 0)
    }

    companion object {
        const val LAST_UPDATE_KEY = "LastUpdate"
        const val EXPIRATION_TIME = 2 * (60 * 60 * 1000)
        const val FILE_NAME = "RepositoriesCache"
    }

}