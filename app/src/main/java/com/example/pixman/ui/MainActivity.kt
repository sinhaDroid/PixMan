package com.example.pixman.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.pixman.BR
import com.example.pixman.R
import com.example.pixman.databinding.ActivityMainBinding
import com.example.pixman.ui.base.BaseActivity
import com.example.pixman.ui.view.EditImageFragment
import com.example.pixman.ui.view.HomeFragment
import com.example.pixman.ui.viewmodel.MainViewModel
import com.example.pixman.util.Constants.RequestKeys.Companion.CAMERA_REQUEST
import com.example.pixman.util.Constants.RequestKeys.Companion.PICK_REQUEST
import com.example.pixman.util.getFragmentInstance
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val layoutRes: Int
        get() = R.layout.activity_main

    override fun viewModel(): Class<MainViewModel> = MainViewModel::class.java

    override fun bindingVariable(): Int = BR.main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment_main)

        // Set up toolbar with navigation
//        setupActionBarWithNavController(navController, AppBarConfiguration(setOf(R.id.home)))

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.editImage -> makeFullScreen()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST -> {
                    val photo = data?.extras!!["data"] as Bitmap
                    getFragmentInstance<HomeFragment>(
                        supportFragmentManager,
                        R.id.nav_host_fragment_main
                    ) {
                        navigateToEdit(photo)
                    }
                }
                PICK_REQUEST -> try {
                    val uri = data?.data
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    getFragmentInstance<HomeFragment>(
                        supportFragmentManager,
                        R.id.nav_host_fragment_main
                    ) {
                        navigateToEdit(bitmap)
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment_main).navigateUp()

    override fun onBackPressed() {
        val navController = findNavController(R.id.nav_host_fragment_main)
        val currentDestination = navController.currentDestination
        if (currentDestination != null) {
            when (currentDestination.id) {
                R.id.editImage -> getFragmentInstance<EditImageFragment>(
                    supportFragmentManager,
                    R.id.nav_host_fragment_main
                ) {
                    onBackPressed()
                }
                else -> super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }
}