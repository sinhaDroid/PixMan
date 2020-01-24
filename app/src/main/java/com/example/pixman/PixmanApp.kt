package com.example.pixman

import android.app.Application
import android.content.Context
import com.example.pixman.di.AppInjector
import com.example.pixman.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject


class PixmanApp : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    private val appComponent by lazy {
        DaggerAppComponent.builder()
            .application(this)
            .build()
    }

    val context: Context
        get() = photoApp!!.context

    companion object {
        operator fun get(context: Context): PixmanApp = context.applicationContext as PixmanApp

        var photoApp: PixmanApp? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        photoApp = this

        appComponent.inject(this)

        AppInjector.init(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        photoApp = null
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector
}