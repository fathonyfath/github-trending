package id.fathonyfath.githubtrending.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import id.fathonyfath.githubtrending.MainActivity

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity

}