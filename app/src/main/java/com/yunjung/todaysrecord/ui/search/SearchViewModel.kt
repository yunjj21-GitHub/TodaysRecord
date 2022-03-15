package com.yunjung.todaysrecord.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yunjung.todaysrecord.models.PhotoStudio
import com.yunjung.todaysrecord.network.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel : ViewModel() {
    private val _searchResults = MutableLiveData<List<PhotoStudio>>()

    val searchResults : LiveData<List<PhotoStudio>>
        get() = _searchResults

    fun updateSearchResults(searchWord : String){
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO){
                try {
                    RetrofitManager.service.getPSListBySearchWord(searchWord)
                }catch (e : Throwable){
                    listOf()
                }
            }
            _searchResults.value = response
        }
    }
}