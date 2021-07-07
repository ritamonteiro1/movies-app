package com.example.tokenlab.activity

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.tokenlab.R
import com.example.tokenlab.extensions.createLoadingDialog
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    private var loginEmailTextInputLayout: TextInputLayout? = null
    private var loginPasswordTextInputLayout: TextInputLayout? = null
    private var loginEmailEditText: EditText? = null
    private var loginPasswordEditText: EditText? = null
    private var loginButton: Button? = null
    private var loadingDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        findViewsById()
        loadingDialog = this.createLoadingDialog()
    }

    private fun findViewsById() {
        loginEmailTextInputLayout = findViewById(R.id.loginEmailTextInputLayout)
        loginPasswordTextInputLayout = findViewById(R.id.loginPasswordTextInputLayout)
        loginEmailEditText = findViewById(R.id.loginEmailEditText)
        loginPasswordEditText = findViewById(R.id.loginPasswordEditText)
        loginButton = findViewById(R.id.loginButton)
    }
}