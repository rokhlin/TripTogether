package com.selfapps.triptogether.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.selfapps.triptogether.repository.AuthRepository
import java.lang.ClassCastException

class AuthViewModelFactory(private val repository: AuthRepository): ViewModelProvider.NewInstanceFactory() {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
       return when (modelClass.canonicalName) {
            RegisterViewModel::class.java.canonicalName -> RegisterViewModel(repository) as T
            LoginViewModel::class.java.canonicalName -> LoginViewModel(repository) as T
            RestoreAuthViewModel::class.java.canonicalName ->  RestoreAuthViewModel(repository) as T
            else -> throw ClassCastException("wrong ViewModel class")
        }
    }
}