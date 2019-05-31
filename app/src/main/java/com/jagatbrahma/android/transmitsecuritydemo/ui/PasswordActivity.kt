package com.jagatbrahma.android.transmitsecuritydemo.ui

import android.os.Bundle
import android.widget.EditText
import com.jagatbrahma.android.transmitsecuritydemo.R
import com.jagatbrahma.android.transmitsecuritydemo.sdk.TransmitSDK

class PasswordActivity : BaseAuthenticationActivity() {
    override fun getAuthenticator() = TransmitSDK.Authenticator.PASSWORD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<EditText>(R.id.input_field).hint = getString(R.string.your_password_here)
    }

}
