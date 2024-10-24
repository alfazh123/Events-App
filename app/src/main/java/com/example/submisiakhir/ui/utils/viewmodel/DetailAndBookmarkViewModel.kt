package com.example.submisiakhir.ui.utils.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submisiakhir.data.EventRepository
import com.example.submisiakhir.data.local.entity.FavoriteEntity
import com.example.submisiakhir.data.remote.response.Event
import com.example.submisiakhir.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch

class DetailAndBookmarkViewModel(private val eventRepository: EventRepository): ViewModel() {

    fun setEvent(eventEntity: FavoriteEntity) {
        viewModelScope.launch {
            eventRepository.addFavorite(eventEntity)
        }
    }

    companion object {
        const val TAG = "DetailBookmarkViewModel"
    }

    fun deleteFromFavorite(favoriteEntity: FavoriteEntity) {
        viewModelScope.launch {
            eventRepository.deleteFromFavorite(favoriteEntity)
        }
    }

    fun getFavorite(): LiveData<List<FavoriteEntity>> {
        _isLoading.value = true
        val favoriteEvents = eventRepository.getFavorite()
        _isLoading.value = false
        return favoriteEvents
    }

    private val _eventdetail = MutableLiveData<Event>()
    val eventDetail: LiveData<Event> = _eventdetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun isFavorite(id: Int): LiveData<FavoriteEntity> {
        return eventRepository.isFavorite(id)
    }

    suspend fun getEvent(id: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventDetail(id)
        try {
            _isLoading.value = false
            _eventdetail.value = client.event
        } catch (e: Exception) {
            _errorMessage.value = e.message
        }
    }
}