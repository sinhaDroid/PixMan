package com.example.pixman.di.module

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.pixman.data.prefs.PixmanPreferences
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
object StorageModule {

    @Provides
    @Reusable
    @JvmStatic
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application)
    }

    @Provides
    @Reusable
    @JvmStatic
    fun providePixmanPreferences(
        sharedPreferences: SharedPreferences,
        gson: Gson
    ): PixmanPreferences {
        return PixmanPreferences(sharedPreferences, gson)
    }
}