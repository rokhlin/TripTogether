package com.selfapps.triptogether.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.selfapps.triptogether.*
import com.selfapps.triptogether.repository.AuthRepository
import kotlinx.coroutines.*

class RegisterViewModel(private val repository: AuthRepository): ScopedViewModel(){
    private val authResponse = MutableLiveData<AuthResponse>()
    fun getAuthState() = authResponse as LiveData<AuthResponse>

    private val progressIsVisible: MutableLiveData<Boolean> = MutableLiveData()
    fun getProgressState() = progressIsVisible as LiveData<Boolean>




    fun registerUser(sUserName:String, sEmail:String, sPassword:String)= uiScope.launch {
       progressIsVisible.value = true

       val registration = withContext(bgDispatcher) {
                                repository.registerUser(sPassword, sEmail,sUserName) as AuthResponse }

       if(registration.response == Resp.SUCCESSFUL) {
           val userDataUpdate = withContext(bgDispatcher){
                                    repository.updateCurrentUser(sPassword, sEmail,sUserName) as AuthResponse}
           authResponse.postValue( userDataUpdate)
       } else
           authResponse.postValue(registration) // if some error happen we'll receive message

        progressIsVisible.value = false

    //
    //       load(this){
    //           launch {
    //               val resp = repository.registerUser(sPassword, sEmail,sUserName) as AuthResponse
    //               if(resp.response == Resp.SUCCESSFUL) authResponse.postValue( repository.updateCurrentUser(sPassword, sEmail,sUserName) as AuthResponse)
    //               else authResponse.postValue(resp) // if some error happen
    //           }
    //          return@load authResponse
    //       }.then{
    //         // progressIsVisible.postValue(false)
    //      }
    //      return  authResponse
    }





}

