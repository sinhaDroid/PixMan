package com.example.pixman.ui.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.pixman.BR
import com.example.pixman.R
import com.example.pixman.databinding.FragmentHomeBinding
import com.example.pixman.ui.base.BaseFragment
import com.example.pixman.ui.viewmodel.MainViewModel
import com.example.pixman.util.Constants.RequestKeys.Companion.CAMERA_REQUEST
import com.example.pixman.util.Constants.RequestKeys.Companion.PICK_REQUEST
import java.io.IOException


class HomeFragment : BaseFragment<FragmentHomeBinding, MainViewModel>() {

    override val layoutRes: Int
        get() = R.layout.fragment_home

    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java

    override fun bindingVariable(): Int = BR.home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.camera.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST)
        }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST -> {
                    val photo = data?.extras!!["data"] as Bitmap
                    navigateToEdit(photo)
                }
                PICK_REQUEST -> try {
                    val uri = data?.data
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
                    navigateToEdit(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}