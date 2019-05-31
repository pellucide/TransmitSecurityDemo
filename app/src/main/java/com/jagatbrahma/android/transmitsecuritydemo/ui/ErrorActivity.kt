package com.jagatbrahma.android.transmitsecuritydemo.ui

import android.os.Bundle
import com.jagatbrahma.android.transmitsecuritydemo.R
import com.jagatbrahma.android.transmitsecuritydemo.sdk.TransmitSDK
import kotlinx.android.synthetic.main.error_activity.*

class ErrorActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.error_activity)
        retry.setOnClickListener { finish() }
        val authenticator: TransmitSDK.Authenticator? = viewModel.lastAuthenticator
        error_message.text = viewModel.authenticatorErrorStrings[authenticator]
    }

}
