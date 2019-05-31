package com.jagatbrahma.android.transmitsecuritydemo.viewmodel

import android.app.Activity.*
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import com.jagatbrahma.android.transmitsecuritydemo.sdk.TransmitSDK.*

class AppViewModel : ViewModel() {
    private val _authenticationResult = MutableLiveData< MutableMap<Authenticator, Int>>()
    val authenticatorErrorStrings : MutableMap<Authenticator, String> = mutableMapOf()
    var lastAuthenticator :Authenticator ? = null
    var authenticators : List<Authenticator> = emptyList()
        set(value) {
            field = value
            val results : MutableMap<Authenticator, Int> = mutableMapOf()
            authenticators.forEach { authenticator ->
                val status: Int = getAuthenticatorStatus(authenticator)
                results[authenticator] = status
            }
            _authenticationResult.value = results
        }


    fun authenticate(authenticator: Authenticator, input: Any) {
        val listener = object:OnResult {
            override fun onComplete() {
                onComplete(authenticator)
            }

            override fun onReject(error: String?) {
                onReject(authenticator, error)
            }
        }
        when (authenticator) {
            Authenticator.PASSWORD -> {
                getInstance().authenticateWithPassword(input as String, listener)
            }
            Authenticator.PINCODE -> {
                getInstance().authenticateWithPincode(input as String, listener)
            }
            Authenticator.FINGERPRINT -> {
                getInstance().authenticateWithFingerprint(input as Boolean, listener)
            }
        }
    }

    fun onComplete(authenticator: Authenticator) {
        val results : MutableMap<Authenticator, Int> = mutableMapOf()
        authenticators.forEach { authenticator1 ->
            val status: Int = getAuthenticatorStatus(authenticator1)
            results[authenticator1] = status
        }
        results[authenticator] = RESULT_OK
        authenticatorErrorStrings[authenticator] = ""
        lastAuthenticator = authenticator
        _authenticationResult.value = results
    }

    fun onReject(authenticator: Authenticator, error: String?) {
        val nonNull : String = error?:""

        val results : MutableMap<Authenticator, Int> = mutableMapOf()
        authenticators.forEach { authenticator1 ->
            val status: Int = getAuthenticatorStatus(authenticator1)
            results[authenticator1] = status
        }
        results[authenticator] = RESULT_CANCELED
        authenticatorErrorStrings[authenticator] = nonNull
        lastAuthenticator = authenticator
        _authenticationResult.value = results

    }

    fun observeAuthenticationResult(owner: LifecycleOwner, observer: Observer<MutableMap<Authenticator, Int>>) {
        _authenticationResult.removeObservers(owner)
        _authenticationResult.observe(owner, observer)
    }

    fun getAuthenticatorStatus(authenticator: Authenticator): Int {
        return _authenticationResult.value?.get(authenticator) ?: RESULT_FIRST_USER
    }

    fun getActiveAuthenticators(): List<Authenticator> {
        val activeAuthenticators: MutableList<Authenticator> = mutableListOf()
        authenticators.forEach { authenticator ->
            val status:Int = getAuthenticatorStatus(authenticator)
            if (status != RESULT_OK)
                activeAuthenticators.add(authenticator)
        }
        return activeAuthenticators
    }
}
