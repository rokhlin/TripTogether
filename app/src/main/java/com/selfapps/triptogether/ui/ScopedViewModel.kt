package com.selfapps.triptogether.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

open class ScopedViewModel: ViewModel(){
    private val job = SupervisorJob()
    protected val uiScope = CoroutineScope(Dispatchers.Main + job)
    protected val bgDispatcher: CoroutineDispatcher = Dispatchers.IO


    override fun onCleared() {
        super.onCleared()
        uiScope.coroutineContext.cancelChildren()
    }
}