package com.selfapps.triptogether.repository


import com.google.firebase.auth.FirebaseUser
import com.selfapps.triptogether.SimpleResponse


interface AuthRepository:IRepository {
    suspend fun registerUser(password: String, email: String, sUserName: String): SimpleResponse
    fun getCurrentUser() : FirebaseUser?
    fun getUserId(user: FirebaseUser): String
    suspend fun updateCurrentUser(sPassword: String, sEmail: String, sUserName: String): SimpleResponse
}