package com.selfapps.triptogether.repository


import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.selfapps.triptogether.*
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class AuthRepositoryImpl(private val auth: FirebaseAuth): AuthRepository {


    private var user: FirebaseUser? = null
    override fun getCurrentUser() = auth.currentUser

    override suspend fun registerUser(password: String, email: String, sUserName: String): SimpleResponse = suspendCoroutine<AuthResponse> { continuation ->
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                Log.d("tEST", "isSuccessful = ${it.isSuccessful}")
                try {
                    continuation.resumeWith(kotlin.runCatching {
                        if (it.isSuccessful) {
                            user = getCurrentUser()
                            if (user != null) return@runCatching AuthResponse(Resp.SUCCESSFUL)
                        }
                        return@runCatching AuthResponse(message = "Create user error: ${it.exception?.message}")
                    })
                } catch (e: Exception){
                    continuation.resumeWithException(e)
                }
            }
        }

    override suspend fun updateCurrentUser(sPassword: String, sEmail: String, sUserName: String): SimpleResponse =
        suspendCancellableCoroutine{
        try {
            if (user != null) {
                val userId = getUserId(user!!)
                val map = hashMapOf(
                    Constants.FIELD_USER_ID to userId,
                    Constants.FIELD_USER_NAME to sUserName,
                    Constants.FIELD_ROLE to Role.USER.name,
                    Constants.FIELD_USER_IMAGE to Constants.DEFAULT_IMAGE,
                    Constants.FIELD_STATUS to Status.OFFLINE.name,
                    Constants.FIELD_LAST_ACTIVE to System.currentTimeMillis().toString()
                )

                FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(map[Constants.FIELD_USER_ID]!!)
                    .setValue(map).addOnCompleteListener { task ->
                        if(task.isSuccessful) it.resume(AuthResponse(Resp.SUCCESSFUL))
                        else it.resume(AuthResponse(message = "Update user database error: ${task.exception?.message}"))
                    }
            } else {
                it.resume(AuthResponse(message = "Create user error: 15 "))
            }



        } catch (e: java.lang.Exception){
            it.resumeWithException(e)
        }
    }


    override fun getUserId(user: FirebaseUser): String {
        //TODO replace with DAO impl
        val databaseReference = FirebaseDatabase.getInstance()
            .getReference("users")
        return databaseReference.child(user.uid).key.toString()
    }
}