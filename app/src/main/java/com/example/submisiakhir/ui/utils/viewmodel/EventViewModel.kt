package com.example.submisiakhir.ui.utils.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submisiakhir.data.remote.response.EventResponse
import com.example.submisiakhir.data.remote.response.ListEventsItem
import com.example.submisiakhir.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventViewModel : ViewModel() {

    private val _active = MutableLiveData<List<ListEventsItem>>()
    val active: LiveData<List<ListEventsItem>> = _active

    private val _completed = MutableLiveData<List<ListEventsItem>>()
    val completed: LiveData<List<ListEventsItem>> = _completed

    private val _isLoadingUpcoming = MutableLiveData<Boolean>()
    val isLoadingUpcoming: LiveData<Boolean> = _isLoadingUpcoming

    private val _isLoadingCompleted = MutableLiveData<Boolean>()
    val isLoadingCompleted: LiveData<Boolean> = _isLoadingCompleted

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    init {
        viewModelScope.launch {
            getActiveEvent()
            getFinishedEvent()
        }
    }

    companion object {
        const val TAG = "FetchEventData"
    }

    private suspend fun getActiveEvent() {
        _isLoadingUpcoming.value = true
        val client = ApiConfig.getApiService().getEventActive()

        try {
            _isLoadingUpcoming.value = false
            _active.value = client.listEvents
        } catch (e: Exception) {
            _isLoadingUpcoming.value = false
            _errorMessage.value = e.message
        }
    }

    private suspend fun getFinishedEvent() {
        _isLoadingCompleted.value = true
        val client = ApiConfig.getApiService().getEventFinished()

        try {
            _isLoadingCompleted.value = false
            _completed.value = client.listEvents
        } catch (e: Exception) {
            _isLoadingCompleted.value = false
            _errorMessage.value = e.message
        }
    }

}