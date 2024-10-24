package com.example.submisiakhir.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.submisiakhir.data.local.entity.FavoriteEntity
import com.example.submisiakhir.data.local.room.EventDao
import com.example.submisiakhir.data.remote.response.DetailEventResponse
import com.example.submisiakhir.data.remote.retrofit.ApiService
import com.example.submisiakhir.utils.AppExecutors

class EventRepository(
    private val apiService: ApiService,
    private val eventDao: EventDao,
    private val appExecutors: AppExecutors
) {

    private val result = MutableLiveData<Result<List<FavoriteEntity>>>()

    suspend fun addFavorite(favoriteEntity: FavoriteEntity) {
        result.postValue(Result.Loading)
        eventDao.insertFavorite(favoriteEntity)
    }

    suspend fun deleteFromFavorite(favoriteEntity: FavoriteEntity) {
        result.postValue(Result.Loading)
        eventDao.deleteFavorite(favoriteEntity)
    }

    fun getFavorite(): LiveData<List<FavoriteEntity>> {
        result.postValue(Result.Loading)
        val favorite = eventDao.getFavorite().value
        if (favorite != null) {
            result.postValue(Result.Success(favorite))
        } else {
            result.postValue(Result.Error("Data not found"))
        }
        return eventDao.getFavorite()
    }

    fun isFavorite(id: Int): LiveData<FavoriteEntity> {
        return eventDao.isFavorite(id)
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            eventDao: EventDao,
            appExecutors: AppExecutors
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, eventDao, appExecutors)
            }.also { instance = it }
    }

}