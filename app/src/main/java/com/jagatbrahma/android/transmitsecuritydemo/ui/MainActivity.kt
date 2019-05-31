package com.jagatbrahma.android.transmitsecuritydemo.ui

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jagatbrahma.android.transmitsecuritydemo.R
import com.jagatbrahma.android.transmitsecuritydemo.sdk.TransmitSDK.Authenticator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private lateinit var authenticatorAdapter: AuthenticationMethodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.authenticationMethods).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context)
            authenticatorAdapter = AuthenticationMethodAdapter()
            adapter = authenticatorAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            visibility = View.INVISIBLE
        }

        if (viewModel.authenticators.isNotEmpty()) {
            authenticatorAdapter.notifyDataSetChanged()
        }

        viewModel.observeAuthenticationResult(this, Observer {
            if (!it.isNullOrEmpty()) {
                recyclerView.visibility = View.VISIBLE
                progress_circular.visibility = View.GONE
            }
            authenticatorAdapter.notifyDataSetChanged()
        })
    }

    inner class AuthenticationMethodAdapter: RecyclerView.Adapter<AuthenticationMethodAdapter.ViewHolder>() {
        inner class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
            val textView1: TextView = view.findViewById(android.R.id.text1)

            init {
                view.findViewById<TextView>(android.R.id.text2) .apply {
                    setText(R.string.subhead)
                    setTextColor(resources.getColor(android.R.color.darker_gray, null))
                }
            }
        }

        override fun getItemCount(): Int =  viewModel.getActiveAuthenticators().size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val authenticator: Authenticator = viewModel.getActiveAuthenticators().elementAt(position)
            val resultCode = viewModel.getAuthenticatorStatus(authenticator)

            holder.view.setBackgroundResource(android.R.color.transparent)
            holder.textView1.text = authenticator.name
            holder.view.isClickable = resultCode != Activity.RESULT_CANCELED

            when (resultCode) {
                Activity.RESULT_CANCELED ->
                    holder.view.setBackgroundResource(android.R.color.holo_red_light)
                else ->
                    holder.view.visibility = View.VISIBLE
            }


            when (authenticator) {
                Authenticator.PASSWORD -> {
                    when {
                        holder.view.isClickable -> holder.view.setOnClickListener {
                            startActivity(Intent(this@MainActivity, PasswordActivity::class.java))
                        }
                    }
                }

                Authenticator.FINGERPRINT -> {
                    when {
                        holder.view.isClickable -> holder.view.setOnClickListener {
                            AlertDialog.Builder(this@MainActivity, 0).apply {
                                setMessage(R.string.fingerprint_authenticator)
                                setPositiveButton(R.string.use_fingerprint) { _: DialogInterface, _: Int ->
                                    viewModel.authenticate(Authenticator.FINGERPRINT, true)
                                }
                                setNegativeButton(android.R.string.cancel, null)
                            }.create().show()
                        }
                    }

                }

                Authenticator.PINCODE -> {
                    when {
                        holder.view.isClickable -> holder.view.setOnClickListener {
                            startActivity(Intent(this@MainActivity, PincodeActivity::class.java))
                        }
                    }
                }
            }
        }
    }
}
