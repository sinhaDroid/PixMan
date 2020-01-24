package com.example.pixman.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pixman.ui.viewmodel.MainViewModel
import com.example.pixman.ui.viewmodel.PixmanViewModelFactory
import com.openapp.tusk.di.scope.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(rentViewModel: MainViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: PixmanViewModelFactory): ViewModelProvider.Factory
}