package com.example.pixman.ui.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pixman.BR
import com.example.pixman.R
import com.example.pixman.databinding.ActivityEditImageBinding
import com.example.pixman.ui.base.BaseActivity
import com.example.pixman.ui.viewmodel.MainViewModel
import com.example.pixman.util.*
import com.example.pixman.util.EditingToolsAdapter.OnItemSelected
import com.example.pixman.util.PhotoEditor.OnSaveListener
import java.io.File
import java.io.IOException

class EditImageActivity : BaseActivity<ActivityEditImageBinding, MainViewModel>(),
    OnPhotoEditorListener,
    View.OnClickListener, PropertiesBSFragment.Properties, OnItemSelected {
    lateinit var mPhotoEditor: PhotoEditor
    private var mPhotoEditorView: PhotoEditorView? = null
    private var mPropertiesBSFragment: PropertiesBSFragment? = null
    private var mTxtCurrentTool: TextView? = null
    private var mRvTools: RecyclerView? = null
    private val mEditingToolsAdapter = EditingToolsAdapter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        makeFullScreen()
        super.onCreate(savedInstanceState)
        initViews()
        mPropertiesBSFragment = PropertiesBSFragment()
        mPropertiesBSFragment!!.setPropertiesChangeListener(this)
        val llmTools =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mRvTools!!.layoutManager = llmTools
        mRvTools!!.adapter = mEditingToolsAdapter
        mPhotoEditor = PhotoEditor.Builder(this, mPhotoEditorView)
            .setPinchTextScalable(true) // set flag to make text scalable when pinch
            .build() // build photo editor sdk
        mPhotoEditor.setOnPhotoEditorListener(this)
        // Get data from intent
        val intent = intent
        val bitmapIntent = intent.getParcelableExtra<Parcelable>("BitmapImage") as Intent
        val uri = bitmapIntent.data
        var bitmap: Bitmap? = null
        try {
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            //Set Image Dynamically
            mPhotoEditorView!!.source.setImageBitmap(bitmap)
        } catch (e: IOException) {
            e.printStackTrace()
            finish()
        }
    }

    private fun initViews() {
        mPhotoEditorView = findViewById(R.id.photoEditorView)
        mTxtCurrentTool = findViewById(R.id.txtCurrentTool)
        mRvTools = findViewById(R.id.rvConstraintTools)
        val imgUndo: ImageView = findViewById(R.id.imgUndo)
        imgUndo.setOnClickListener(this)
        val imgRedo: ImageView = findViewById(R.id.imgRedo)
        imgRedo.setOnClickListener(this)
        val imgSave: ImageView = findViewById(R.id.imgSave)
        imgSave.setOnClickListener(this)
        val imgClose: ImageView = findViewById(R.id.imgClose)
        imgClose.setOnClickListener(this)
    }

    override fun onEditTextChangeListener(
        rootView: View,
        text: String,
        colorCode: Int
    ) {
        val textEditorDialogFragment =
            TextEditorDialogFragment.show(
                this,
                text,
                colorCode
            )
        textEditorDialogFragment.setOnTextEditorListener(object :
            TextEditorDialogFragment.TextEditor {
            override fun onDone(inputText: String?, colorCode: Int) {
                val styleBuilder = TextStyleBuilder()
                styleBuilder.withTextColor(colorCode)
                mPhotoEditor.editText(rootView, inputText, styleBuilder)
                mTxtCurrentTool!!.setText(R.string.label_text)
            }

        })
    }

    override fun onAddViewListener(
        viewType: ViewType,
        numberOfAddedViews: Int
    ) {
        Log.d(
            TAG,
            "onAddViewListener() called with: viewType = [$viewType], numberOfAddedViews = [$numberOfAddedViews]"
        )
    }

    override fun onRemoveViewListener(
        viewType: ViewType,
        numberOfAddedViews: Int
    ) {
        Log.d(
            TAG,
            "onRemoveViewListener() called with: viewType = [$viewType], numberOfAddedViews = [$numberOfAddedViews]"
        )
    }

    override fun onStartViewChangeListener(viewType: ViewType) {
        Log.d(
            TAG,
            "onStartViewChangeListener() called with: viewType = [$viewType]"
        )
    }

    override fun onStopViewChangeListener(viewType: ViewType) {
        Log.d(
            TAG,
            "onStopViewChangeListener() called with: viewType = [$viewType]"
        )
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.imgUndo -> mPhotoEditor!!.undo()
            R.id.imgRedo -> mPhotoEditor!!.redo()
            R.id.imgSave -> saveImage()
            R.id.imgClose -> onBackPressed()
        }
    }

    @SuppressLint("MissingPermission")
    private fun saveImage() {
        if (requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            showLoading("Saving...")
            val file = File(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator + ""
                        + System.currentTimeMillis() + ".png"
            )
            try {
                file.createNewFile()
                val saveSettings = SaveSettings.Builder()
                    .setClearViewsEnabled(true)
                    .setTransparencyEnabled(true)
                    .build()
                mPhotoEditor.saveAsFile(
                    file.absolutePath,
                    saveSettings,
                    object : OnSaveListener {
                        override fun onSuccess(imagePath: String) {
                            hideLoading()
                            showSnackbar("Image Saved Successfully")
                            mPhotoEditorView!!.source
                                .setImageURI(Uri.fromFile(File(imagePath)))
                        }

                        override fun onFailure(exception: Exception) {
                            hideLoading()
                            showSnackbar("Failed to save Image")
                        }
                    })
            } catch (e: IOException) {
                e.printStackTrace()
                hideLoading()
                showSnackbar(e.message!!)
            }
        }
    }

    override fun onColorChanged(colorCode: Int) {
        mPhotoEditor!!.brushColor = colorCode
        mTxtCurrentTool!!.setText(R.string.label_brush)
    }

    override fun onOpacityChanged(opacity: Int) {
        mPhotoEditor!!.setOpacity(opacity)
        mTxtCurrentTool!!.setText(R.string.label_brush)
    }

    override fun onBrushSizeChanged(brushSize: Int) {
        mPhotoEditor.brushSize = brushSize.toFloat()
        mTxtCurrentTool!!.setText(R.string.label_brush)
    }

    override fun isPermissionGranted(
        isGranted: Boolean,
        permission: String?
    ) {
        if (isGranted) {
            saveImage()
        }
    }

    private fun showSaveDialog() {
        val builder =
            AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.msg_save_image))
        builder.setPositiveButton("Save") { dialog, which -> saveImage() }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
        builder.setNeutralButton("Discard") { dialog, which -> finish() }
        builder.create().show()
    }

    override fun onToolSelected(toolType: ToolType) {
        when (toolType) {
            ToolType.BRUSH -> {
                mPhotoEditor!!.setBrushDrawingMode(true)
                mTxtCurrentTool!!.setText(R.string.label_brush)
                mPropertiesBSFragment!!.show(
                    supportFragmentManager,
                    mPropertiesBSFragment!!.tag
                )
            }
            ToolType.TEXT -> {
                val textEditorDialogFragment =
                    TextEditorDialogFragment.show(
                        this
                    )
                textEditorDialogFragment.setOnTextEditorListener(object :
                    TextEditorDialogFragment.TextEditor {
                    override fun onDone(inputText: String?, colorCode: Int) {
                        val styleBuilder = TextStyleBuilder()
                        styleBuilder.withTextColor(colorCode)
                        mPhotoEditor.addText(inputText, styleBuilder)
                        mTxtCurrentTool!!.setText(R.string.label_text)
                    }

                })
            }
            ToolType.ERASER -> {
                mPhotoEditor!!.brushEraser()
                mTxtCurrentTool!!.setText(R.string.label_eraser_mode)
            }
        }
    }

    override fun onBackPressed() {
        if (!mPhotoEditor!!.isCacheEmpty) {
            showSaveDialog()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private val TAG = EditImageActivity::class.java.simpleName
    }

    override val layoutRes: Int
        get() = R.layout.activity_edit_image

    override fun viewModel(): Class<MainViewModel> = MainViewModel::class.java

    override fun bindingVariable(): Int = BR.main
}