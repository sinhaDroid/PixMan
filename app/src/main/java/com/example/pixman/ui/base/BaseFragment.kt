package com.example.pixman.ui.base

import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.example.pixman.R
import com.example.pixman.di.Injectable
import com.example.pixman.ui.binding.FragmentDataBindingComponent
import com.example.pixman.util.autoCleared
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

/**
 *
 * @version 0.1.2
 * @since 5/25/2018
 */
abstract class BaseFragment<DB : ViewDataBinding, VM : ViewModel> : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var dataBinding by autoCleared<DB>()

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    lateinit var viewModel: VM

    private var mProgressDialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, viewModelFactory).get(getViewModel())

        dataBinding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        dataBinding.apply {
            lifecycleOwner = lifecycleOwner
            setVariable(bindingVariable(), viewModel)
            executePendingBindings()
        }

        return dataBinding.root
    }

    fun <T> LiveData<T>.reObserve(owner: LifecycleOwner, observer: Observer<T>) {
        removeObserver(observer)
        observe(owner, observer)
    }

    fun requestPermission(permission: String): Boolean {
        val isGranted =
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED
        if (!isGranted) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(permission),
                BaseActivity.READ_WRITE_STORAGE
            )
        }
        return isGranted
    }

    open fun isPermissionGranted(
        isGranted: Boolean,
        permission: String?
    ) {
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            BaseActivity.READ_WRITE_STORAGE -> isPermissionGranted(
                grantResults[0] == PackageManager.PERMISSION_GRANTED,
                permissions[0]
            )
        }
    }

    fun showLoading(message: String) {
        mProgressDialog = ProgressDialog(requireContext())
        mProgressDialog!!.setMessage(message)
        mProgressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        mProgressDialog!!.setCancelable(false)
        mProgressDialog!!.show()
    }

    fun hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog!!.dismiss()
        }
    }

    fun showSnackbar(message: String) {
        val view = view?.findViewById<View>(R.id.content)
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutRes: Int

    abstract fun getViewModel(): Class<VM>

    abstract fun bindingVariable(): Int
}