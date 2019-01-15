package com.selfapps.triptogether.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.selfapps.triptogether.Constants.DEFAULT_IMAGE
import com.selfapps.triptogether.Constants.FIELD_LAST_ACTIVE
import com.selfapps.triptogether.Constants.FIELD_ROLE
import com.selfapps.triptogether.Constants.FIELD_STATUS
import com.selfapps.triptogether.Constants.FIELD_USER_ID
import com.selfapps.triptogether.Constants.FIELD_USER_IMAGE
import com.selfapps.triptogether.Constants.FIELD_USER_NAME
import com.selfapps.triptogether.R
import com.selfapps.triptogether.Role
import com.selfapps.triptogether.Status
import kotlinx.android.synthetic.main.fragment_register.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class RegisterFragment : Fragment(), KodeinAware {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    override val kodein by closestKodein()
    private val auth: FirebaseAuth by instance()
    private var databaseReference: DatabaseReference? = null

    private lateinit var registerAction: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        registerAction = view.findViewById(R.id.btn_register) as Button
        registerAction.setOnClickListener{
            register()
        }
        return view
    }

    private fun register() {
        //TODO Add field check
        val sEmail:String = email.text.toString()
        val sPassword: String = password.text.toString()
        val sUserName: String = name.text.toString()
        auth.createUserWithEmailAndPassword(sEmail,sPassword).addOnCompleteListener {
            task ->
            if(task.isSuccessful){
                val user = auth.currentUser
                if(user != null){
                    val userId = getUserId(user)
                    val map = hashMapOf(FIELD_USER_ID to userId,
                        FIELD_USER_NAME to sUserName,
                        FIELD_ROLE to Role.USER.name,
                        FIELD_USER_IMAGE to DEFAULT_IMAGE,
                        FIELD_STATUS to Status.OFFLINE.name,
                        FIELD_LAST_ACTIVE to System.currentTimeMillis().toString())
                    saveData(map, task)

                }
            } else {
                showStatus("Create user error: ${task.exception?.message}")
            }
        }
    }

    private fun saveData(map: HashMap<String, String>, task: Task<AuthResult>) {
        databaseReference!!.child(map[FIELD_USER_ID]!!).setValue(map).addOnCompleteListener {
            if(task.isSuccessful){
                changeFragment(StartFragment.newInstance(), R.id.root_container)
            }else {
                showStatus("Update user database error: ${task.exception?.message}")
            }
        }
    }

    private fun getUserId(user: FirebaseUser): String {
        databaseReference = FirebaseDatabase.getInstance()
            .getReference("users")
        return databaseReference!!.child(user.uid).key.toString()
    }

    private fun showStatus(s: String) {
        Toast.makeText(context,s, Toast.LENGTH_SHORT).show()
    }


}
