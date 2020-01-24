package com.example.pixman.ui.view

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.pixman.BR
import com.example.pixman.R
import com.example.pixman.databinding.FragmentHomeBinding
import com.example.pixman.ui.base.BaseFragment
import com.example.pixman.ui.viewmodel.MainViewModel
import com.example.pixman.util.Constants.RequestKeys.Companion.PICK_REQUEST

class HomeFragment : BaseFragment<FragmentHomeBinding, MainViewModel>() {

    override val layoutRes: Int
        get() = R.layout.fragment_home

    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java

    override fun bindingVariable(): Int = BR.home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.camera.setOnClickListener {  }

        dataBinding.gallery.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_REQUEST)
        }
    }

    internal fun navigateToEdit(bitmap: Bitmap) {
        findNavController().navigate(HomeFragmentDirections.actionHomeToEditImage(bitmap))
    }
}