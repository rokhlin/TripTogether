package com.selfapps.triptogether.ui

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.suspendCancellableCoroutine
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


object CallBackExtractor {
    suspend fun <T> awaitComplete(block: (OnCompleteListener<T>) -> Unit) : Task<T> =
        suspendCancellableCoroutine { cont ->
            try {
                block(OnCompleteListener { task ->
                    cont.resume(task) })
            } catch (e: Exception){
                e.let { cont.resumeWithException(it) }
            }

        }

    fun <T> awaitCompleted (fn: (OnCompleteListener<T>) -> Unit): suspend ()-> Task<T> = {
        awaitComplete { fn(it) }
    }


//    fun <A, T> toSuspendFunction (fn: (A, Callback<T>) -> Unit): suspend (A)-> T = { a: A ->
//        awaitCallback { fn(a, it) }
//    }
//
//    fun <A, B, T> toSuspendFunction (fn: (A, B, Callback<T>) -> Unit): suspend (A, B)-> T = { a: A, b: B ->
//        awaitCallback { fn(a, b, it) }
//    }
//    suspend fun <T> awaitCallback(block: (Callback<T>) -> Unit) : T =
//        suspendCancellableCoroutine { cont ->
//            block(object : Callback<T> {
//                override fun onComplete(result: T) = cont.resume(result)
//                override fun onException(e: Exception?) {
//                    e?.let { cont.resumeWithException(it) }
//                }
//            })
//        }
}




