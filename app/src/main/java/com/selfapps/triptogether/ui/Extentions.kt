package com.selfapps.triptogether.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*


inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}


fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int){
    supportFragmentManager.inTransaction { add(frameId, fragment) }
}


fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction{replace(frameId, fragment)}
}

fun Fragment.changeFragment(fragment: Fragment, frameId: Int) {
    (activity as AuthActivity).supportFragmentManager.inTransaction{replace(frameId, fragment)}
}


fun <T> Fragment.load(scope: CoroutineScope,loadFunction: () -> T): Deferred<T> {
    return scope.async { loadFunction() }
}

fun <T> ViewModel.load(scope: CoroutineScope,loadFunction: () -> T): Deferred<T> {
    return scope.async { loadFunction() }
}

suspend fun <T> ViewModel.loadWithResult(scope: CoroutineScope, loadFunction: () -> T): T {
    return scope.async { loadFunction() }.await()
}


fun <T> Deferred<T>.then(uiFunction: (T) -> Unit) {
    GlobalScope.launch(Dispatchers.Main) { return@launch uiFunction(this@then.await())}
}