package com.selfapps.triptogether.repository


import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.selfapps.triptogether.*
import com.selfapps.triptogether.ui.CallBackExtractor


class AuthRepositoryImpl(private val auth: FirebaseAuth): AuthRepository {


    private var user: FirebaseUser? = null
    override fun getCurrentUser() = auth.currentUser


    override suspend fun registerUser(password: String, email: String, sUserName: String): SimpleResponse {
        try {
            val result = CallBackExtractor.awaitComplete<AuthResult> {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(it)
            }

            if (result.isSuccessful) {
                user = getCurrentUser()
                if (user != null) return AuthResponse(Resp.SUCCESSFUL)
            }
        }catch (e: Exception){
            AuthResponse(message = "Create user error: ${e.message}")
        }

        return AuthResponse(message = "Create user error: 10")

    }

    override suspend fun updateCurrentUser(sPassword: String, sEmail: String, sUserName: String): SimpleResponse =
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

                val res = CallBackExtractor.awaitComplete<Void> { FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(userId)
                    .setValue(map).addOnCompleteListener(it) }

                if(res.isSuccessful) AuthResponse(Resp.SUCCESSFUL)
                else AuthResponse(message = "Update user database error: ${res.exception?.message}")
            } else {
                AuthResponse(message = "Update user error: 15 ")
            }
        } catch (e: Exception){
            AuthResponse(message = "Update user error:16 ${e.message}")
        }



    override fun getUserId(user: FirebaseUser): String {
        //TODO replace with DAO impl
        val databaseReference = FirebaseDatabase.getInstance()
            .getReference("users")
        return databaseReference.child(user.uid).key.toString()
    }
}