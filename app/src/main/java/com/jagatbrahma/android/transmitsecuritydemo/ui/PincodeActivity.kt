package com.jagatbrahma.android.transmitsecuritydemo.ui

import android.os.Bundle
import android.text.InputFilter
import android.widget.EditText
import com.jagatbrahma.android.transmitsecuritydemo.R
import com.jagatbrahma.android.transmitsecuritydemo.sdk.TransmitSDK

class PincodeActivity : BaseAuthenticationActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<EditText>(R.id.input_field).apply{
            hint = getString(R.string.your_pincode_here)
            filters = Array<InputFilter>(1) { InputFilter.LengthFilter(5)}
        }
    }

    override fun getAuthenticator() = TransmitSDK.Authenticator.PINCODE
}
