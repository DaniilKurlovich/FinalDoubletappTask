package com.example.finaltaskandroid.data.di

import android.app.Application

class FinalTaskApplication: Application() {
    lateinit var dependencyFactory: DependencyFactory
        private set

    override fun onCreate() {
        super.onCreate()
        dependencyFactory = DependencyFactory()
    }
}