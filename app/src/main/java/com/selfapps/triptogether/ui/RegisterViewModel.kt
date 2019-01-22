package com.selfapps.triptogether.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.selfapps.triptogether.*
import com.selfapps.triptogether.repository.AuthRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class RegisterViewModel(private val repository: AuthRepository): ViewModel(), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
    private val authResponse = MutableLiveData<AuthResponse>()


    private val progressIsVisible: MutableLiveData<Boolean> = MutableLiveData()

   fun registerUser(sUserName:String, sEmail:String, sPassword:String): LiveData<AuthResponse> {
       progressIsVisible.value = true



       load(this){
            //var response = AuthResponse()

           launch {
              authResponse.postValue( repository.registerUser(sPassword, sEmail,sUserName) as AuthResponse)
           }
          return@load authResponse
       }.then{
          progressIsVisible.postValue(false)
      }
      return  authResponse
   }

    fun getProgressState() = progressIsVisible as LiveData<Boolean>


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}

