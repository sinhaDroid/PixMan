package com.example.pixman.ui.view

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pixman.BR
import com.example.pixman.R
import com.example.pixman.databinding.FragmentEditImageBinding
import com.example.pixman.ui.MainActivity
import com.example.pixman.ui.base.BaseFragment
import com.example.pixman.ui.viewmodel.MainViewModel
import com.example.pixman.util.*
import com.example.pixman.util.EditingToolsAdapter.OnItemSelected
import java.io.File
import java.io.IOException

class EditImageFragment : BaseFragment<FragmentEditImageBinding, MainViewModel>(),
    OnPhotoEditorListener,
    View.OnClickListener, PropertiesBSFragment.Properties, OnItemSelected {

    override val layoutRes: Int
        get() = R.layout.fragment_edit_image

    override fun bindingVariable(): Int = BR.main

    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java

    lateinit var mPhotoEditor: PhotoEditor
    private var mPropertiesBSFragment: PropertiesBSFragment? = null
    private val mEditingToolsAdapter = EditingToolsAdapter(this)

    private val args: EditImageFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.imgUndo.setOnClickListener(this)
        dataBinding.imgRedo.setOnClickListener(this)
        dataBinding.imgSave.setOnClickListener(this)
        dataBinding.imgClose.setOnClickListener(this)

        val llmTools =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        dataBinding.rvConstraintTools.layoutManager = llmTools
        dataBinding.rvConstraintTools.adapter = mEditingToolsAdapter
        mPhotoEditor = PhotoEditor.Builder(requireContext(), dataBinding.photoEditorView)
            .setPinchTextScalable(true) // set flag to make text scalable when pinch
            .build() // build photo editor sdk

        mPropertiesBSFragment = PropertiesBSFragment()
        mPropertiesBSFragment!!.setPropertiesChangeListener(this)
        mPhotoEditor.setOnPhotoEditorListener(this)

        // Get data from navigation args
        //Set Image Dynamically
        dataBinding.photoEditorView.source.setImageBitmap(args.bitmapData)
    }

    override fun onEditTextChangeListener(
        rootView: View,
        text: String,
        colorCode: Int
    ) {
        val textEditorDialogFragment =
            TextEditorDialogFragment.show(
                requireActivity() as MainActivity,
                text,
                colorCode
            )
        textEditorDialogFragment.setOnTextEditorListener(object :
            TextEditorDialogFragment.TextEditor {
            override fun onDone(inputText: String?, colorCode: Int) {
                val styleBuilder = TextStyleBuilder()
                styleBuilder.withTextColor(colorCode)
                mPhotoEditor.editText(rootView, inputText, styleBuilder)
                dataBinding.txtCurrentTool.text = getString(R.string.label_text)
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
            R.id.imgUndo -> mPhotoEditor.undo()
            R.id.imgRedo -> mPhotoEditor.redo()
            R.id.imgSave -> saveImage()
            R.id.imgClose -> requireActivity().onBackPressed()
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
                    object : PhotoEditor.OnSaveListener {
                        override fun onSuccess(imagePath: String) {
                            hideLoading()
                            showSnackbar("Image Saved Successfully")
                            dataBinding.photoEditorView.source
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
        mPhotoEditor.brushColor = colorCode
        dataBinding.txtCurrentTool.text = getString(R.string.label_brush)
    }

    override fun onOpacityChanged(opacity: Int) {
        mPhotoEditor.setOpacity(opacity)
        dataBinding.txtCurrentTool.text = getString(R.string.label_brush)
    }

    override fun onBrushSizeChanged(brushSize: Int) {
        mPhotoEditor.brushSize = brushSize.toFloat()
        dataBinding.txtCurrentTool.text = getString(R.string.label_brush)
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
            AlertDialog.Builder(requireContext())
        builder.setMessage(getString(R.string.msg_save_image))
        builder.setPositiveButton("Save") { dialog, which -> saveImage() }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
        builder.setNeutralButton("Discard") { dialog, which -> requireActivity().finish() }
        builder.create().show()
    }

    override fun onToolSelected(toolType: ToolType) {
        when (toolType) {
            ToolType.BRUSH -> {
                mPhotoEditor.setBrushDrawingMode(true)
                dataBinding.txtCurrentTool.text = getString(R.string.label_brush)
                mPropertiesBSFragment!!.show(
                    requireFragmentManager(),
                    mPropertiesBSFragment!!.tag
                )
            }
            ToolType.TEXT -> {
                val textEditorDialogFragment =
                    TextEditorDialogFragment.show(
                        requireActivity() as MainActivity
                    )
                textEditorDialogFragment.setOnTextEditorListener(object :
                    TextEditorDialogFragment.TextEditor {
                    override fun onDone(inputText: String?, colorCode: Int) {
                        val styleBuilder = TextStyleBuilder()
                        styleBuilder.withTextColor(colorCode)
                        mPhotoEditor.addText(inputText, styleBuilder)
                        dataBinding.txtCurrentTool.text = getString(R.string.label_text)
                    }

                })
            }
            ToolType.ERASER -> {
                mPhotoEditor.brushEraser()
                dataBinding.txtCurrentTool.text = getString(R.string.label_eraser_mode)
            }
        }
    }

    fun onBackPressed() {
        if (!mPhotoEditor.isCacheEmpty) {
            showSaveDialog()
        } else {
            findNavController().popBackStack()
        }
    }

    companion object {
        private val TAG = EditImageFragment::class.java.simpleName
    }
}