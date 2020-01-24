package com.example.pixman.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }

    /*@Provides
    @Singleton
    fun provideUtils(context: Context): Utils {
        return Utils(context)
    }*/
}