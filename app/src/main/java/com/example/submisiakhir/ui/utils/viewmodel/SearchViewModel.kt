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

class SearchViewModel : ViewModel() {

    companion object {
        const val TAG = "SearchViewModel"
    }

    private val _query = MutableLiveData<String>()

    private val _results = MutableLiveData<List<ListEventsItem>>()
    val results: LiveData<List<ListEventsItem>> = _results

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun setQuery(newQuery: String) {
        if (_query.value == newQuery) return
        _query.value = newQuery
        viewModelScope.launch {
            searchResult(newQuery)
        }
    }

    private suspend fun searchResult(q: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventSearch(-1,q)
        try {
            _isLoading.value = false
            _results.value = client.listEvents
        } catch (e: Exception) {
            _isLoading.value = false
            _errorMessage.value = e.message
        }
    }

}