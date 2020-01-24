package com.example.pixman.ui.base

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.pixman.util.SingleLiveEvent

/**
 * Created by deepanshu on 11/01/18.
 */

open class BaseViewModel : ViewModel() {

    val loading = ObservableField<Boolean>()

    val showSuccessSheet = SingleLiveEvent<String>()
    val showErrorSheet = SingleLiveEvent<String>()
    val showErrorToast = SingleLiveEvent<String>()
}