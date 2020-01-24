package com.example.pixman.di.module

import com.example.pixman.ui.MainActivity
import com.openapp.tusk.di.scope.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [])
    abstract fun contributeMainActivity(): MainActivity
}