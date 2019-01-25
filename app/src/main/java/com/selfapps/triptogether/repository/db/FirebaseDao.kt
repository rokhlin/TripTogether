package com.selfapps.triptogether.repository.db

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.selfapps.triptogether.Constants
import com.selfapps.triptogether.Constants.FB_TABLE_USERS
import com.selfapps.triptogether.Role
import com.selfapps.triptogether.Status
import com.selfapps.triptogether.ui.CallBackExtractor

class FirebaseDao: IDao {
    private val auth = FirebaseAuth.getInstance()
    private val databaseRootReference = FirebaseDatabase.getInstance()
    private val usersReference = databaseRootReference.getReference(FB_TABLE_USERS)


    var currentUser: FirebaseUser? =  null
        get() = auth.currentUser

    private fun requestUserId(): String = usersReference.child(currentUser!!.uid).key.toString()


    suspend fun createUser(email: String, password: String) = CallBackExtractor.awaitComplete<AuthResult> {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(it)
    }

    suspend fun saveCurrentUser(sUserName: String, userId: String): Task<Void> {
        val map = hashMapOf(
            Constants.FIELD_USER_ID to userId,
            Constants.FIELD_USER_NAME to sUserName,
            Constants.FIELD_ROLE to Role.USER.name,
            Constants.FIELD_USER_IMAGE to Constants.DEFAULT_IMAGE,
            Constants.FIELD_STATUS to Status.OFFLINE.name,
            Constants.FIELD_LAST_ACTIVE to System.currentTimeMillis().toString()
        )

        return CallBackExtractor.awaitComplete{
            usersReference.child(requestUserId())
                .setValue(map).addOnCompleteListener(it)
        }
    }
}