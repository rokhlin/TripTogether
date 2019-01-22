package com.selfapps.triptogether.repository


import com.google.firebase.auth.FirebaseUser
import com.selfapps.triptogether.SimpleResponse
import kotlinx.coroutines.Deferred

interface AuthRepository:IRepository {
    suspend fun registerUser(password: String, email: String, sUserName: String): SimpleResponse
    fun getCurrentUser() : FirebaseUser?
    fun getUserId(user: FirebaseUser): String
    fun updateCurrentUser(map: HashMap<String, String>): SimpleResponse
}