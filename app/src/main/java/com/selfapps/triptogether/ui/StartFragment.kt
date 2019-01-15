package com.selfapps.triptogether.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.selfapps.triptogether.R


class StartFragment : Fragment() {

    companion object {
        fun newInstance() = StartFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_start, container, false)

        val doRegister = view.findViewById(R.id.tv_do_register) as TextView
        doRegister.setOnClickListener {
            changeFragment(RegisterFragment.newInstance(), R.id.root_container)
        }
        return view
    }


}
