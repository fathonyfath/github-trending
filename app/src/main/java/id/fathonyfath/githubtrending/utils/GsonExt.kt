package id.fathonyfath.githubtrending.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> Gson.fromJson(json: String): T? {
    val typeToken = object : TypeToken<T>() {}
    return this.fromJson(json, typeToken.type)
}