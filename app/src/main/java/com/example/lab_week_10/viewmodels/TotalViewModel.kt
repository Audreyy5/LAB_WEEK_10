package com.example.lab_week_10.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TotalViewModel : ViewModel() {
    val total = MutableLiveData<Int>()

    init {
        total.value = 0
    }

    fun incrementTotal() {
        total.value = (total.value ?: 0) + 1
    }

    fun setTotal(newTotal: Int) {
        total.value = newTotal
    }
}