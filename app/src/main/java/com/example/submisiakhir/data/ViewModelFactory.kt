package com.example.submisiakhir.data

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.submisiakhir.di.Injection
import com.example.submisiakhir.ui.utils.viewmodel.DetailAndBookmarkViewModel

class ViewModelFactory private constructor(private val eventRepository: EventRepository) :
   ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailAndBookmarkViewModel::class.java)) {
            return DetailAndBookmarkViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.providerRepository(context))
            }.also { instance = it }
    }
}