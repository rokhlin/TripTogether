package com.selfapps.triptogether.repository.db

class FbDatabase:IDatabase<FirebaseDao> {
    override val dao = FirebaseDao()
}