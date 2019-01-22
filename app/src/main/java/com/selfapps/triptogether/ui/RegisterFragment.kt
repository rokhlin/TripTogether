package com.selfapps.triptogether.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.selfapps.triptogether.AuthResponse
import com.selfapps.triptogether.R
import com.selfapps.triptogether.Resp
import com.selfapps.triptogether.Utils
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance





class RegisterFragment : Fragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: AuthViewModelFactory by instance()


    companion object {
        fun newInstance() = RegisterFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        val viewModel = ViewModelProviders.of(this,viewModelFactory).get(RegisterViewModel::class.java)

        subscribeProgressBar(viewModel)
        registerNewUser(viewModel, view)

        return view
    }

    private fun registerNewUser(viewModel: RegisterViewModel, view: View) {
        val registerAction = view.findViewById(R.id.btn_register) as Button
        registerAction.setOnClickListener {
            val sEmail: String = email.text.toString()
            val sPassword: String = password.text.toString()
            val sUserName: String = name.text.toString()

            if (validateFields(sUserName, sPassword, sEmail)) {
                viewModel.registerUser(sUserName, sEmail, sPassword).observe(this, Observer { result ->
                    when (result.response) {
                        Resp.SUCCESSFUL -> changeFragment(StartFragment.newInstance(), R.id.root_container)
                        Resp.ERROR -> showStatus(result.message)
                    }
                })
            }
        }
    }

//    private suspend fun doRegister(
//        sEmail: String,
//        sPassword: String,
//        sUserName: String,
//        viewModel: RegisterViewModel
//    ): AuthResponse  {
//        return
//    }
//}


//    viewModel.registerUser(sUserName,sEmail,sPassword).also { result ->
//        when(result.response){
//            Resp.SUCCESSFUL -> changeFragment(StartFragment.newInstance(), R.id.root_container)
//            Resp.ERROR -> showStatus(result.message)
//        }
//    }

    private fun validateFields(sUserName: String, sPassword: String, sEmail: String):Boolean {
        if(!Utils.checkEmail(sEmail)) showStatus("Wrong Email!").also { return false }
        if(!Utils.checkPassword(sPassword)) showStatus("Password should to be in range 6..30 symbols!").also { return false }
        if(!Utils.checkUserName(sUserName)) showStatus("User Name should to be in range 6..30 symbols!").also { return false }
        return true
    }

    private fun subscribeProgressBar(viewModel: RegisterViewModel) {
        viewModel.getProgressState().observe(this, Observer { isVisible ->
            showProgress(isVisible)
        })
    }

    private fun showStatus(s: String) {
        Toast.makeText(context,s, Toast.LENGTH_SHORT).show()
    }


    private fun showProgress(setVisible:Boolean){
        login_progress.visibility = if(setVisible) View.VISIBLE else View.GONE
    }


}
