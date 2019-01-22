package com.selfapps.triptogether.repository.db

interface IDatabase<out T : IDao> {
    val dao: T
}