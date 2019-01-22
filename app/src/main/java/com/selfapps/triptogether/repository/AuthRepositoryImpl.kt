package com.selfapps.triptogether.repository


import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import com.google.firebase.database.FirebaseDatabase
import com.selfapps.triptogether.*
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class AuthRepositoryImpl(private val auth: FirebaseAuth): AuthRepository {


    override fun getCurrentUser() = auth.currentUser

    override suspend fun registerUser(password: String, email: String, sUserName: String): SimpleResponse = suspendCoroutine<AuthResponse> { continuation ->
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                Log.d("tEST", "isSuccessful = ${it.isSuccessful}")
                try {
                    continuation.resumeWith(kotlin.runCatching {

                            if (it.isSuccessful) {
                                AuthResponse(response = Resp.SUCCESSFUL)
                            } else {
                                AuthResponse(message = "Create user error: ${it.exception?.message}")
                            }


                    })
                } catch (e: Exception){
                    continuation.resumeWithException(e)
                }


//                if (it.isSuccessful) {
//                    continuation.resume(AuthResponse(response = Resp.SUCCESSFUL))
//                } else {
//                    continuation.resume(AuthResponse(message = "Create user error: ${it.exception?.message}"))
//                }
            }
        }

//       var response = SimpleResponse()
//       //val res:Result<SimpleResponse> =  Result.success(response)
//
//        //auth.createUserWithEmailAndPassword(email,password)
//            longFunc(email,password).addOnCompleteListener {
//            if (it.isSuccessful) {
//                val user = getCurrentUser()
//                if (user != null) {
//                    val userId = getUserId(user)
//                    val map = hashMapOf(
//                        Constants.FIELD_USER_ID to userId,
//                        Constants.FIELD_USER_NAME to sUserName,
//                        Constants.FIELD_ROLE to Role.USER.name,
//                        Constants.FIELD_USER_IMAGE to Constants.DEFAULT_IMAGE,
//                        Constants.FIELD_STATUS to Status.OFFLINE.name,
//                        Constants.FIELD_LAST_ACTIVE to System.currentTimeMillis().toString()
//                    )
//                    response = updateCurrentUser(map) as AuthResponse
//                }
//            } else response = AuthResponse(Resp.ERROR, "Create user error: ${it.exception?.message}")
//
//            }


 //      return response




    override fun updateCurrentUser(map: HashMap<String, String>): SimpleResponse {
        val result = AuthResponse()
        //TODO replace with DAO impl
        FirebaseDatabase.getInstance()
            .getReference("users")
            .child(map[Constants.FIELD_USER_ID]!!)
            .setValue(map).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    result.response = Resp.SUCCESSFUL
                }else {
                    result.message = "Update user database error: ${task.exception?.message}"
                    Result.failure<SimpleResponse>(task.exception!!)
                }
            }
       return result
    }

    override fun getUserId(user: FirebaseUser): String {
        //TODO replace with DAO impl
        val databaseReference = FirebaseDatabase.getInstance()
            .getReference("users")
        return databaseReference.child(user.uid).key.toString()
    }

//    private fun saveData(map: HashMap<String, String>) {
//
//        FirebaseDatabase.getInstance()
//            .getReference("users")
//            .child(map[Constants.FIELD_USER_ID]!!)
//            .setValue(map).addOnCompleteListener { task ->
//            if(task.isSuccessful){
//                changeFragment(StartFragment.newInstance(), R.id.root_container)
//            }else {
//                showStatus("Update user database error: ${task.exception?.message}")
//            }
//        }
//    }
}