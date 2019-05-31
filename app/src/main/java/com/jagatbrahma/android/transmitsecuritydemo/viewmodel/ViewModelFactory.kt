package com.jagatbrahma.android.transmitsecuritydemo.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.annotation.VisibleForTesting

class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(AppViewModel::class.java) ->
                        getViewModel()
                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T

    companion object {

        @Volatile private var INSTANCE: ViewModelFactory? = null
        @Volatile private var viewModelInstance: AppViewModel? = null

        fun getInstance() =
                INSTANCE
                        ?: synchronized(ViewModelFactory::class.java) {
                    INSTANCE
                            ?: ViewModelFactory().also { INSTANCE = it }
                }



        fun getViewModel() =
                viewModelInstance
                        ?: synchronized(ViewModelFactory::class.java) {
                    viewModelInstance
                            ?: AppViewModel().also { viewModelInstance = it }
                }


        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
