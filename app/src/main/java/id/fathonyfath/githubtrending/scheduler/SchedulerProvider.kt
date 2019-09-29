package id.fathonyfath.githubtrending.scheduler

import io.reactivex.Scheduler

interface SchedulerProvider {

    fun main(): Scheduler
    fun computation(): Scheduler
    fun io(): Scheduler
}