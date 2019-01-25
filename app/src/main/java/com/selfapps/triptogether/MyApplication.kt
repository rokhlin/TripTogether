package com.selfapps.triptogether

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.selfapps.triptogether.repository.AuthRepository
import com.selfapps.triptogether.repository.AuthRepositoryImpl
import com.selfapps.triptogether.repository.db.FbDatabase
import com.selfapps.triptogether.repository.db.FirebaseDao
import com.selfapps.triptogether.ui.AuthViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MyApplication: Application(),KodeinAware {
    override val kodein = Kodein.lazy {
        bind<FbDatabase>() with singleton { FbDatabase() }
        bind<FirebaseDao>() with singleton { instance<FbDatabase>().dao }
        //bind<FirebaseAuth>() with singleton { FirebaseAuth.getInstance() }

        //Auth module //TODO extract to module
        bind<AuthRepository>() with singleton { AuthRepositoryImpl(instance()) }
        bind() from provider { AuthViewModelFactory(instance()) }
    }
}