package com.selfapps.triptogether.repository


import com.selfapps.triptogether.AuthResponse
import com.selfapps.triptogether.Resp
import com.selfapps.triptogether.SimpleResponse
import com.selfapps.triptogether.repository.db.FirebaseDao


class AuthRepositoryImpl(private val dao: FirebaseDao): AuthRepository {

    override suspend fun registerUser(password: String, email: String, sUserName: String): SimpleResponse {
        try {
            val result = dao.createUser(email, password)
            if (result.isSuccessful) {
                if (dao.currentUser != null) return AuthResponse(Resp.SUCCESSFUL)
            }
        }catch (e: Exception){
            AuthResponse(message = "Create user error: ${e.message}")
        }
        return AuthResponse(message = "Create user error: 10")
    }


    override suspend fun updateCurrentUser(sUserName: String): SimpleResponse =
        try {
            if (dao.currentUser != null) {
                val res = dao.saveCurrentUser(sUserName,dao.currentUser!!.uid)

                if(res.isSuccessful)
                    AuthResponse(Resp.SUCCESSFUL)
                else
                    AuthResponse(message = "Update user database error: ${res.exception?.message}")
            } else {
                AuthResponse(message = "Update user error: 15 ")
            }
        } catch (e: Exception){
            AuthResponse(message = "Update user error:16 ${e.message}")
        }


    override suspend fun isUserLoginAlive(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun loginUser(password: String, email: String): SimpleResponse {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun restorePassword(email: String): SimpleResponse {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}