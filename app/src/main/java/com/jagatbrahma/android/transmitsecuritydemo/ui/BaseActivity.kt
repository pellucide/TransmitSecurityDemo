package com.jagatbrahma.android.transmitsecuritydemo.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelStore
import android.arch.lifecycle.ViewModelStoreOwner
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jagatbrahma.android.transmitsecuritydemo.sdk.TransmitSDK
import com.jagatbrahma.android.transmitsecuritydemo.viewmodel.AppViewModel
import com.jagatbrahma.android.transmitsecuritydemo.viewmodel.ViewModelFactory

abstract class BaseActivity : AppCompatActivity(), ViewModelStoreOwner {
    lateinit var viewModel: AppViewModel
    private val sharedViewModelStore: ViewModelStore by lazy { ViewModelStore() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance()).get(AppViewModel::class.java)

        if (viewModel.authenticators.isNullOrEmpty())
            TransmitSDK.getInstance().apply { authenticatorsList { viewModel.authenticators = it } }
    }

    override fun getViewModelStore(): ViewModelStore = sharedViewModelStore

}
