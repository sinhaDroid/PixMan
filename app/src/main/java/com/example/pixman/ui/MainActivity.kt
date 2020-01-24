package com.example.pixman.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import com.example.pixman.BR
import com.example.pixman.R
import com.example.pixman.databinding.ActivityMainBinding
import com.example.pixman.ui.base.BaseActivity
import com.example.pixman.ui.view.EditImageActivity
import com.example.pixman.ui.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val layoutRes: Int
        get() = R.layout.activity_main

    override fun viewModel(): Class<MainViewModel> = MainViewModel::class.java

    override fun bindingVariable(): Int = BR.main

    private val CAMERA_REQUEST = 52
    private val PICK_REQUEST = 53

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_REQUEST)
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
                    val photo = data?.extras!!["data"] as Bitmap?
                    startEditActivity(data)
                }
                PICK_REQUEST -> try {
                    val uri = data?.data
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    startEditActivity(data)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun startEditActivity(data: Intent?) {
        val intent = Intent(this, EditImageActivity::class.java)
        intent.putExtra("BitmapImage", data)
        startActivity(intent)
    }
}