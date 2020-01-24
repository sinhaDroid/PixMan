package com.example.pixman.di.module

import com.example.pixman.ui.view.EditImageFragment
import com.example.pixman.ui.view.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeEditImageFragment(): EditImageFragment
}