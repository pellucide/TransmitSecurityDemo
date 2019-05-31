package com.jagatbrahma.android.transmitsecuritydemo.ui

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.jagatbrahma.android.transmitsecuritydemo.R
import com.jagatbrahma.android.transmitsecuritydemo.sdk.TransmitSDK

abstract class BaseAuthenticationActivity : BaseActivity() {
    abstract fun getAuthenticator(): TransmitSDK.Authenticator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.authenticate_with_input_field)
        val input:EditText? = findViewById(R.id.input_field)
        findViewById<Button>(R.id.authenticate)?.setOnClickListener {
            viewModel.authenticate(getAuthenticator(),input?.text.toString())
        }
        viewModel.observeAuthenticationResult(this, Observer { r: MutableMap<TransmitSDK.Authenticator, Int>? ->
            val authenticator: TransmitSDK.Authenticator = getAuthenticator()
            r?.get(authenticator)?.let {
                when (it) {
                    Activity.RESULT_CANCELED -> {
                        val intent = Intent(this, ErrorActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    Activity.RESULT_OK -> finish()
                }
            }
        })
    }
}
