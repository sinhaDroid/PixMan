package com.example.pixman.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import com.example.pixman.BR
import com.example.pixman.R
import com.example.pixman.databinding.ActivityMainBinding
import com.example.pixman.ui.base.BaseActivity
import com.example.pixman.ui.view.EditImageFragment
import com.example.pixman.ui.viewmodel.MainViewModel
import com.example.pixman.util.getFragmentInstance
import kotlinx.android.synthetic.main.activity_main.*


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

        /*navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.editImage -> makeFullScreen()
            }
        }*/
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