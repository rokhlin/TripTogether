package com.selfapps.triptogether.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.selfapps.triptogether.R

/**
 * A login screen that offers login via email/password.
 */
class AuthActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        if (savedInstanceState == null) {
            addFragment(
                StartFragment.newInstance(),
                R.id.root_container
            )
        }
    }


}
