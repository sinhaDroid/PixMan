package com.example.pixman.ui.view

import android.os.Bundle
import android.view.View
import com.example.pixman.BR
import com.example.pixman.R
import com.example.pixman.databinding.FragmentHomeBinding
import com.example.pixman.ui.base.BaseFragment
import com.example.pixman.ui.viewmodel.MainViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding, MainViewModel>() {

    override val layoutRes: Int
        get() = R.layout.fragment_home

    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java

    override fun bindingVariable(): Int = BR.home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}