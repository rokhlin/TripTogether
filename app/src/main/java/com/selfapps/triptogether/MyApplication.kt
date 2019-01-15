package com.selfapps.triptogether

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class MyApplication: Application(),KodeinAware {
    override val kodein = Kodein.lazy {
        bind<FirebaseAuth>() with singleton { FirebaseAuth.getInstance() }
    }
}