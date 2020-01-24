package com.example.pixman.di.module

import com.example.pixman.ui.MainActivity
import com.example.pixman.ui.view.EditImageFragment
import com.example.pixman.ui.view.HomeFragment
import com.openapp.tusk.di.scope.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeMainActivity(): MainActivity
}