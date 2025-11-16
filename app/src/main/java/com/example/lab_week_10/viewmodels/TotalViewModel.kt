package com.example.lab_week_10.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab_week_10.database.TotalObject
import java.util.Date // Recommended to import for clean usage

class TotalViewModel: ViewModel() {
    private val _total = MutableLiveData<TotalObject>()
    val total: LiveData<TotalObject> = _total

    init {
        _total.postValue(TotalObject())
    }

    fun incrementTotal() {
        val currentObject = _total.value ?: TotalObject()

        // When incrementing, we use the long Date().toString() format.
        // The cleanup (formatting) is done in MainActivity.onPause() before saving to Room.
        val newObject = currentObject.copy(
            value = currentObject.value + 1,
            date = Date().toString()
        )
        _total.postValue(newObject)
    }

    fun setTotal(newTotalObject: TotalObject) {
        _total.postValue(newTotalObject)
    }
}