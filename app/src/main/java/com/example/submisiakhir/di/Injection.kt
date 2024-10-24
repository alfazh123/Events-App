package com.example.submisiakhir.di

import android.content.Context
import com.example.submisiakhir.data.EventRepository
import com.example.submisiakhir.data.local.room.EventDatabase
import com.example.submisiakhir.data.remote.retrofit.ApiConfig
import com.example.submisiakhir.utils.AppExecutors

object Injection {
    fun providerRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val dao = database.eventDao()
        val appExecutors = AppExecutors()
        return EventRepository(apiService, dao, appExecutors)
    }
}