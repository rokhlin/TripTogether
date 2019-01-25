package com.selfapps.triptogether.repository

import com.selfapps.triptogether.SimpleResponse


interface AuthRepository:IRepository {
    suspend fun registerUser(password: String, email: String, sUserName: String): SimpleResponse
    suspend fun updateCurrentUser(sUserName: String): SimpleResponse
    suspend fun isUserLoginAlive(): Boolean
    suspend fun loginUser(password: String, email: String):SimpleResponse
    suspend fun restorePassword(email: String): SimpleResponse

}