package com.example.pixman.di.component

import android.app.Application
import com.example.pixman.PixmanApp
import com.example.pixman.di.module.ActivityModule
import com.example.pixman.di.module.AppModule
import com.example.pixman.di.module.StorageModule
import com.example.pixman.di.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ActivityModule::class,
        AndroidSupportInjectionModule::class,
        AppModule::class,
        StorageModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: PixmanApp)
}